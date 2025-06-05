package com.sjy.imagechain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjy.imagechain.domain.AppUser;
import com.sjy.imagechain.domain.NFTInfo;
import com.sjy.imagechain.domain.NFTTransaction;
import com.sjy.imagechain.mapper.AppUserMapper;
import com.sjy.imagechain.mapper.NFTInfoMapper;
import com.sjy.imagechain.mapper.NFTTransactionMapper;
import com.sjy.imagechain.service.AppUserService;
import com.sjy.imagechain.service.NFTService;
import com.sjy.imagechain.utils.UserUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@Service
public class NFTServiceImpl implements NFTService {

    @Autowired
    private BcosSDK bcosSDK;

    @Autowired
    private NFTInfoMapper nftInfoMapper;

    @Autowired
    private NFTTransactionMapper nftTransactionMapper;

    @Autowired
    private AppUserService  appUserService;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CONTRACT_ADDRESS = "0x320e65107eef9ec10c0c3e21f53505e06c986dc9"; // 实际部署的合约地址

    @Override
    public Map<String, Object> getNFTList(Integer page, Integer pageSize, String query) {

        List<NFTInfo> allNfts = nftInfoMapper.selectNFTList(query);
        Long total = nftInfoMapper.selectNFTListCount(query);

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allNfts.size());

        List<NFTInfo> paginatedNfts;
        if (start < allNfts.size()) {
            paginatedNfts = allNfts.subList(start, end);
        } else {
            paginatedNfts = Collections.emptyList(); // No results for this page
        }

        return Map.of(
                "list", paginatedNfts,
                "total", total
        );
    }

    @Override
    public Map<String, Object> getMyNFTs(Integer page, Integer pageSize, String mode) {
        String userId = userUtils.getCurrentUserId();

        AppUser appUser = appUserMapper.selectById(userId);

        // Fetch all owned NFTs from the mapper
        List<NFTInfo> allMyNfts = nftInfoMapper.selectMyNFTs(appUser.getBlockchainAddress());
        Long total = nftInfoMapper.selectMyNFTsCount(appUser.getBlockchainAddress());

        // Manually apply pagination to the list
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allMyNfts.size());

        List<NFTInfo> paginatedMyNfts;
        if (start < allMyNfts.size()) {
            paginatedMyNfts = allMyNfts.subList(start, end);
        } else {
            paginatedMyNfts = Collections.emptyList(); // No results for this page
        }

        return Map.of(
                "list", paginatedMyNfts,
                "total", total
        );
    }

    @Override
    @Transactional
    public Map<String, Object> mintNFT(String imageId, String description, BigDecimal price, String minioUrl) {
        try {
            // 1. 获取客户端
            Client client = bcosSDK.getClient(1);
//            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
            CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userUtils.getCurrentUserId());
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
            );

            // 2. 调用智能合约铸造NFT
            List<Object> params = new ArrayList<>();
            params.add(keyPair.getAddress()); // 接收者地址
            params.add(description); // NFT描述
            params.add(price.toBigInteger()); // 价格

            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "ImageNFT", CONTRACT_ADDRESS, "mintNFT", params
            );

            // 3. 保存NFT信息到数据库
            NFTInfo nftInfo = new NFTInfo();
            nftInfo.setImageId(imageId);
            nftInfo.setMinioUrl(minioUrl);
            nftInfo.setDescription(description);
            nftInfo.setBlockchainAddress(keyPair.getAddress());
            nftInfo.setTokenId(response.getReturnObject().get(0).toString());
            nftInfo.setContractAddress(CONTRACT_ADDRESS);
            nftInfo.setPrice(price);
            nftInfo.setIsForSale(false);
            nftInfo.setCreateTime(LocalDateTime.now());
            nftInfo.setIsDelete(false);

            nftInfoMapper.insert(nftInfo);


            return Map.of(
                    "nftId", nftInfo.getNftId(),
                    "tokenId", nftInfo.getTokenId(),
                    "transactionHash", response.getTransactionReceipt().getTransactionHash()
            );
        } catch (Exception e) {
            throw new RuntimeException("铸造NFT失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> buyNFT(String nftId) {
        try {
            // 获取NFT信息
            NFTInfo nftInfo = nftInfoMapper.selectById(nftId);
            if (nftInfo == null || !nftInfo.getIsForSale()) {
                throw new RuntimeException("NFT不存在或未在售");
            }

            AppUser appUser = appUserMapper.selectById(userUtils.getCurrentUserId());
            if (nftInfo.getBlockchainAddress().equals(appUser.getBlockchainAddress())) {
                throw new RuntimeException("不能购买自己的NFT");
            }

            // 获取客户端
            Client client = bcosSDK.getClient(1);
//            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
            CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userUtils.getCurrentUserId());
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
            );

            // 调用智能合约购买NFT
            List<Object> params = new ArrayList<>();
            params.add(nftInfo.getTokenId());

            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "ImageNFT", CONTRACT_ADDRESS, "buyNFT", params
            );

            // 保存交易记录
            NFTTransaction transaction = new NFTTransaction();
            transaction.setNftId(nftId);
            transaction.setFromUserId(nftInfo.getBlockchainAddress());
            transaction.setToUserId(keyPair.getAddress()); // This should be the address of the buyer (msg.sender in contract)
            transaction.setPrice(nftInfo.getPrice());
            transaction.setTransactionHash(response.getTransactionReceipt().getTransactionHash());
            transaction.setCreateTime(LocalDateTime.now());
            transaction.setIsDelete(false);

            nftTransactionMapper.insert(transaction);

            // 更新NFT所有者
            nftInfo.setBlockchainAddress(keyPair.getAddress()); // Update owner to the buyer's address
            nftInfo.setIsForSale(false);
            nftInfo.setUpdateTime(LocalDateTime.now());

            nftInfoMapper.updateById(nftInfo);

            return Map.of(
                    "transactionId", transaction.getTransactionId(),
                    "transactionHash", transaction.getTransactionHash()
            );
        } catch (Exception e) {
            throw new RuntimeException("购买NFT失败", e);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> setNFTPrice(String nftId, BigDecimal price) {
        try {
            // 获取NFT信息
            NFTInfo nftInfo = nftInfoMapper.selectById(nftId);
            if (nftInfo == null) {
                throw new RuntimeException("NFT不存在");
            }

            // 获取客户端
            Client client = bcosSDK.getClient(1);
//            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
            CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userUtils.getCurrentUserId());
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
            );

            // 调用智能合约设置价格
            List<Object> params = new ArrayList<>();
            params.add(nftInfo.getTokenId());
            params.add(price.toBigInteger());

            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "ImageNFT", CONTRACT_ADDRESS, "updatePrice", params
            );

            // 更新NFT价格
            nftInfo.setPrice(price);
            nftInfo.setIsForSale(true); // Setting price usually means it's for sale
            nftInfo.setUpdateTime(LocalDateTime.now());

            nftInfoMapper.updateById(nftInfo);

            return Map.of(
                    "nftId", nftId,
                    "price", price,
                    "transactionHash", response.getTransactionReceipt().getTransactionHash()
            );
        } catch (Exception e) {
            throw new RuntimeException("设置NFT价格失败", e);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> cancelNFTSale(String nftId) {
        try {
            // 获取NFT信息
            NFTInfo nftInfo = nftInfoMapper.selectById(nftId);
            if (nftInfo == null) {
                throw new RuntimeException("NFT不存在");
            }

            // 获取客户端
            Client client = bcosSDK.getClient(1);
//            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
            CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userUtils.getCurrentUserId());
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
            );

            // 调用智能合约取消出售
            List<Object> params = new ArrayList<>();
            params.add(nftInfo.getTokenId());
            params.add(false); // Set isForSale to false

            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "ImageNFT", CONTRACT_ADDRESS, "setForSale", params
            );

            // Update NFT status
            nftInfo.setIsForSale(false);
            nftInfo.setUpdateTime(LocalDateTime.now());

            nftInfoMapper.updateById(nftInfo);

            return Map.of(
                    "nftId", nftId,
                    "transactionHash", response.getTransactionReceipt().getTransactionHash()
            );
        } catch (Exception e) {
            throw new RuntimeException("取消NFT出售失败", e);
        }
    }

    @Override
    public Map<String, Object> getNFTDetail(String nftId) {
        NFTInfo nftInfo = nftInfoMapper.selectById(nftId);
        if (nftInfo == null) {
            throw new RuntimeException("NFT不存在");
        }
        return Map.of("nft", nftInfo);
    }

    @Override
    public Map<String, Object> getNFTTransactions() {
        // 查询并按创建时间降序排序（最新在前）
        List<NFTTransaction> allTransactions = nftTransactionMapper.selectList(
                new QueryWrapper<NFTTransaction>().orderByDesc("create_time")
        );
        Long total = (long) allTransactions.size(); // 计算总条数

        return Map.of(
                "list", allTransactions,
                "total", total
        );
    }



    @Override
    @Transactional
    public Map<String, Object> deposit(BigDecimal amount) {
        try {
            Client client = bcosSDK.getClient(1);
            CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userUtils.getCurrentUserId());
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
            );

            List<Object> params = new ArrayList<>();
            params.add(amount.toBigInteger());

            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "ImageNFT", CONTRACT_ADDRESS, "deposit", params
            );

            return Map.of(
                    "transactionHash", response.getTransactionReceipt().getTransactionHash(),
                    "amount", amount
            );
        } catch (Exception e) {
            throw new RuntimeException("充值失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getBalance() {
        try {
            Client client = bcosSDK.getClient(1);
            CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userUtils.getCurrentUserId());
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
            );

            List<Object> params = new ArrayList<>();
            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "ImageNFT", CONTRACT_ADDRESS, "getBalance", params
            );

            return Map.of(
                    "balance", new BigDecimal(response.getReturnObject().get(0).toString())
            );
        } catch (Exception e) {
            throw new RuntimeException("获取余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getNFTInfo(String tokenId) {
        try {
            // 使用 MyBatis-Plus 根据 tokenId 查询 NFT 信息（非主键）
            NFTInfo nftInfo = nftInfoMapper.selectOne(new QueryWrapper<NFTInfo>()
                    .eq("token_id", tokenId)); // 假设数据库字段名为 token_id

            if (nftInfo == null) {
                throw new RuntimeException("NFT不存在");
            }

            Client client = bcosSDK.getClient(1);
            CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userUtils.getCurrentUserId());
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
            );

            List<Object> params = new ArrayList<>();
            // 确保tokenId是BigInteger类型
            BigInteger tokenIdBigInt = new BigInteger(tokenId);
            params.add(tokenIdBigInt);

            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "ImageNFT", CONTRACT_ADDRESS, "getNFT", params
            );

            List<Object> returnObject = response.getReturnObject();
            if (returnObject == null || returnObject.isEmpty()) {
                throw new RuntimeException("从智能合约获取NFT信息失败");
            }

            return Map.of(
                    "uri", returnObject.get(0).toString(),
                    "description", returnObject.get(1).toString(),
                    "price", new BigDecimal(returnObject.get(2).toString()),
                    "isForSale", Boolean.parseBoolean(returnObject.get(3).toString()),
                    "owner", returnObject.get(4).toString(),
                    "nftId", nftInfo.getNftId(),
                    "minioUrl", nftInfo.getMinioUrl(),
                    "createTime", nftInfo.getCreateTime(),
                    "tokenId", tokenId
            );
        } catch (Exception e) {
            throw new RuntimeException("获取NFT信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getOwnedNFTs() {
        try {
            Client client = bcosSDK.getClient(1);
            CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userUtils.getCurrentUserId());
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
            );

            List<Object> params = new ArrayList<>();
            params.add(keyPair.getAddress());

            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                    "ImageNFT", CONTRACT_ADDRESS, "getOwnedNFTs", params
            );

            List<Object> tokenIds = response.getReturnObject();
            return Map.of(
                    "tokenIds", tokenIds
            );
        } catch (Exception e) {
            throw new RuntimeException("获取拥有的NFT失败: " + e.getMessage(), e);
        }
    }

    private String getCurrentUserId() {
        return userUtils.getCurrentUserId();
    }
}