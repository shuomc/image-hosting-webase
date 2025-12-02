package com.sjy.imagechain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjy.imagechain.domain.AppUser;
import com.sjy.imagechain.domain.NftInfo;
import com.sjy.imagechain.domain.NftTransaction;
import com.sjy.imagechain.mapper.AppUserMapper;
import com.sjy.imagechain.domain.vo.NftTransactionVO;
import com.sjy.imagechain.domain.vo.NftVO;
import com.sjy.imagechain.domain.vo.PageData;
import com.sjy.imagechain.mapper.NftInfoMapper;
import com.sjy.imagechain.mapper.NftTransactionMapper;
import com.sjy.imagechain.service.AppUserService;
import com.sjy.imagechain.service.NftService;
import com.sjy.imagechain.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NftServiceImpl implements NftService {

    @Autowired
    private BcosSDK bcosSDK;

    @Autowired
    private NftInfoMapper nftInfoMapper;

    @Autowired
    private NftTransactionMapper nftTransactionMapper;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private UserUtils userUtils;

    // 合约相关配置
    private static final String CONTRACT_ADDRESS = "0x7b9558d415a181859c0467653bc27cbb7b8799f6";  // ImageNFTv2
    private static final String CONTRACT_NAME = "ImageNFTv2";
    private static final String ABI_PATH = "src/main/resources/abi/";
    private static final String BIN_PATH = "src/main/resources/bin/";
    private static final int GROUP_ID = 1;

    // ==========================================
    // 1. 浏览与查询 (使用 Mapper 的 default 方法)
    // ==========================================

    @Override
    public PageData<NftVO> getNFTList(Integer page, Integer pageSize, String query, String category, String sort) {
        // 1. 使用 Mapper 封装好的逻辑查询全量 List
        List<NftInfo> allNfts = nftInfoMapper.selectMarketList(query, category, sort);
        Long total = nftInfoMapper.selectMarketListCount(query, category);

        // 2. 内存分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allNfts.size());

        List<NftInfo> pagedList;
        if (start < allNfts.size()) {
            pagedList = allNfts.subList(start, end);
        } else {
            pagedList = Collections.emptyList();
        }

        // 3. 批量转换为 VO (解决 N+1 问题)
        List<NftVO> voList = populateNftVOs(pagedList);

        return new PageData<>(voList, total);
    }

    @Override
    public NftVO getNFTDetail(String nftId) {
        NftInfo nftInfo = nftInfoMapper.selectById(nftId);
        if (nftInfo == null) {
            throw new RuntimeException("NFT不存在");
        }
        // 单条转换直接复用 list 逻辑，或者单独查
        return populateNftVOs(Collections.singletonList(nftInfo)).get(0);
    }

    @Override
    public PageData<NftVO> getMyNFTs(Integer page, Integer pageSize, String mode) {
        // 1. 获取当前用户地址
        CryptoKeyPair keyPair = getUserKeyPair();
        String userAddress = keyPair.getAddress();

        // 2. 使用 Mapper 封装好的逻辑
        List<NftInfo> allNfts = nftInfoMapper.selectMyNFTs(userAddress, mode);
        Long total = nftInfoMapper.selectMyNFTsCount(userAddress, mode);

        // 3. 内存分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allNfts.size());

        List<NftInfo> pagedList;
        if (start < allNfts.size()) {
            pagedList = allNfts.subList(start, end);
        } else {
            pagedList = Collections.emptyList();
        }

        // 4. 转换 VO
        List<NftVO> voList = populateNftVOs(pagedList);

        return new PageData<>(voList, total);
    }

    @Override
    public PageData<NftTransactionVO> getMyTransactions(Integer page, Integer pageSize, String type) {
        CryptoKeyPair keyPair = getUserKeyPair();
        String userAddress = keyPair.getAddress();

        // 1. 使用 Mapper 封装好的逻辑
        List<NftTransaction> allTransactions = nftTransactionMapper.selectUserTransactions(userAddress, type);
        Long total = nftTransactionMapper.selectUserTransactionsCount(userAddress, type);

        // 2. 内存分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allTransactions.size());

        List<NftTransaction> pagedList;
        if (start < allTransactions.size()) {
            pagedList = allTransactions.subList(start, end);
        } else {
            pagedList = Collections.emptyList();
        }

        // 3. 转换为 VO 并填充 NFT 信息
        List<NftTransactionVO> voList = pagedList.stream().map(tx -> {
            NftTransactionVO vo = new NftTransactionVO();
            BeanUtils.copyProperties(tx, vo);
            return vo;
        }).collect(Collectors.toList());

        // 4. 批量填充关联的 NFT 信息 (名称、图片)
        if (!voList.isEmpty()) {
            Set<String> nftIds = pagedList.stream().map(NftTransaction::getNftId).collect(Collectors.toSet());
            if (!nftIds.isEmpty()) {
                // 批量查 NFT
                List<NftInfo> nftInfos = nftInfoMapper.selectBatchIds(nftIds);
                Map<String, NftInfo> nftMap = nftInfos.stream()
                        .collect(Collectors.toMap(NftInfo::getNftId, Function.identity()));

                for (NftTransactionVO vo : voList) {
                    // 假设 Transaction 实体里存的是 nftId (UUID)，如果存的是 tokenId 需要另外处理
                    // 这里需要在 NftTransaction 实体里找对应的字段匹配
                    // 由于上一步 copyProperties，我们假设 Transaction 实体里有 nftId 字段
                    // 这里需要反查 pagedList 找到对应的 nftId，因为 VO 可能没包含或者包含
                    // 最简单的办法是 NftTransactionVO 也有 nftId 字段
                    // 此处假设原始 list 和 vo list 顺序一致 (stream map 保证一致)
                    String currentNftId = pagedList.get(voList.indexOf(vo)).getNftId();

                    if (nftMap.containsKey(currentNftId)) {
                        NftInfo nft = nftMap.get(currentNftId);
                        vo.setNftName(nft.getName());
                        vo.setImageUrl(nft.getImageUrl());
                    }
                }
            }
        }

        return new PageData<>(voList, total);
    }

    @Override
    public BigDecimal getBalance() {
        try {
            AssembleTransactionProcessor processor = getProcessor();
            List<Object> params = new ArrayList<>();
            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, "getBalance", params
            );
            handleContractError(response);
            BigInteger balanceWei = (BigInteger) response.getReturnObject().get(0);
            return new BigDecimal(balanceWei);
        } catch (Exception e) {
            log.error("获取余额失败", e);
            throw new RuntimeException("获取余额失败");
        }
    }

    // ==========================================
    // 2. 核心交易 (Write Operations)
    // ==========================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String mintNFT(String imageId, String name, String description, BigDecimal price, Integer collectionId) {
        try {
            CryptoKeyPair keyPair = getUserKeyPair();
            AssembleTransactionProcessor processor = getProcessor(keyPair);

            // 1. 链上交互: mintNFT(minioUrl, description, price)
            // 假设 imageId 即为 url, 实际需拼接
            String minioUrl = imageId;
            List<Object> params = new ArrayList<>();
            params.add(minioUrl);
            params.add(description);
            params.add(price.toBigInteger());

            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, "mintNFT", params
            );
            handleContractError(response);

            List<Object> returnObjects = response.getReturnObject();
            if (returnObjects == null || returnObjects.isEmpty()) {
                throw new RuntimeException("合约未返回 Token ID");
            }
            String tokenId = returnObjects.get(0).toString();

            // 2. 落库
            NftInfo nftInfo = new NftInfo();
            nftInfo.setImageUrl(minioUrl);
            nftInfo.setName(name);
            nftInfo.setDescription(description);
            nftInfo.setPrice(price);
            nftInfo.setOwnerAddress(keyPair.getAddress());
            nftInfo.setCreatorAddress(keyPair.getAddress());
            nftInfo.setTokenId(tokenId);
            nftInfo.setContractAddress(CONTRACT_ADDRESS);
            nftInfo.setCollectionId(collectionId);
            nftInfo.setIsForSale(false);
            nftInfo.setIsDelete(false);
            nftInfo.setCreateTime(LocalDateTime.now());

            nftInfoMapper.insert(nftInfo);

            recordTransaction(nftInfo.getNftId(), "0x0000000000000000000000000000000000000000", keyPair.getAddress(), price, response.getTransactionReceipt().getTransactionHash(), 1); // 1=Success

            return nftInfo.getNftId();
        } catch (Exception e) {
            log.error("铸造NFT失败", e);
            throw new RuntimeException("铸造NFT失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean buyNFT(String nftId) {
        NftInfo nftInfo = nftInfoMapper.selectById(nftId);
        if (nftInfo == null || !nftInfo.getIsForSale()) {
            throw new RuntimeException("NFT不存在或未在售");
        }

        CryptoKeyPair buyerKeyPair = getUserKeyPair();
        if (nftInfo.getOwnerAddress().equalsIgnoreCase(buyerKeyPair.getAddress())) {
            throw new RuntimeException("不能购买自己的NFT");
        }

        try {
            AssembleTransactionProcessor processor = getProcessor(buyerKeyPair);

            List<Object> params = new ArrayList<>();
            params.add(new BigInteger(nftInfo.getTokenId()));

            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, "buyNFT", params
            );
            handleContractError(response);

            String oldOwner = nftInfo.getOwnerAddress();
            nftInfo.setOwnerAddress(buyerKeyPair.getAddress());
            nftInfo.setIsForSale(false);
            nftInfo.setUpdateTime(LocalDateTime.now());
            nftInfoMapper.updateById(nftInfo);

            recordTransaction(nftInfo.getNftId(), oldOwner, buyerKeyPair.getAddress(), nftInfo.getPrice(), response.getTransactionReceipt().getTransactionHash(), 1);

            return true;
        } catch (Exception e) {
            log.error("购买NFT失败", e);
            throw new RuntimeException("购买NFT失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transferNFT(String nftId, String toAddress) {
        NftInfo nftInfo = nftInfoMapper.selectById(nftId);
        if (nftInfo == null) throw new RuntimeException("NFT不存在");

        CryptoKeyPair senderKeyPair = getUserKeyPair();
        if (!nftInfo.getOwnerAddress().equalsIgnoreCase(senderKeyPair.getAddress())) {
            throw new RuntimeException("您不是该NFT的所有者");
        }

        try {
            AssembleTransactionProcessor processor = getProcessor(senderKeyPair);

            List<Object> params = new ArrayList<>();
            params.add(toAddress);
            params.add(new BigInteger(nftInfo.getTokenId()));

            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, "transferNFT", params
            );
            handleContractError(response);

            nftInfo.setOwnerAddress(toAddress);
            nftInfo.setIsForSale(false);
            nftInfo.setUpdateTime(LocalDateTime.now());
            nftInfoMapper.updateById(nftInfo);

            recordTransaction(nftInfo.getNftId(), senderKeyPair.getAddress(), toAddress, BigDecimal.ZERO, response.getTransactionReceipt().getTransactionHash(), 1);

            return true;
        } catch (Exception e) {
            log.error("赠送NFT失败", e);
            throw new RuntimeException("赠送NFT失败");
        }
    }

    // ==========================================
    // 3. 状态与价格管理
    // ==========================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateNFTPrice(String nftId, BigDecimal newPrice) {
        return executeContractAction(nftId, "updatePrice", new Object[]{newPrice.toBigInteger()}, nft -> {
            nft.setPrice(newPrice);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean putOnShelf(String nftId) {
        NftInfo nftInfo = nftInfoMapper.selectById(nftId);
        if(nftInfo.getPrice() == null || nftInfo.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("上架前请先设置有效价格");
        }

        try {
            CryptoKeyPair keyPair = getUserKeyPair();
            AssembleTransactionProcessor processor = getProcessor(keyPair);

            List<Object> params = new ArrayList<>();
            params.add(new BigInteger(nftInfo.getTokenId()));
            params.add(nftInfo.getPrice().toBigInteger());

            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, "listNFT", params
            );
            handleContractError(response);

            nftInfo.setIsForSale(true);
            nftInfoMapper.updateById(nftInfo);
            return true;
        } catch (Exception e) {
            log.error("上架失败", e);
            throw new RuntimeException("上架失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean offShelf(String nftId) {
        try {
            NftInfo nftInfo = nftInfoMapper.selectById(nftId);
            CryptoKeyPair keyPair = getUserKeyPair();
            AssembleTransactionProcessor processor = getProcessor(keyPair);

            List<Object> params = new ArrayList<>();
            params.add(new BigInteger(nftInfo.getTokenId()));

            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, "delistNFT", params
            );
            handleContractError(response);

            nftInfo.setIsForSale(false);
            nftInfoMapper.updateById(nftInfo);
            return true;
        } catch (Exception e) {
            log.error("下架失败", e);
            throw new RuntimeException("下架失败");
        }
    }

    // ==========================================
    // 4. 资金管理
    // ==========================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deposit(BigDecimal amount) {
        try {
            CryptoKeyPair keyPair = getUserKeyPair();
            AssembleTransactionProcessor processor = getProcessor(keyPair);

            List<Object> params = new ArrayList<>();
            params.add(amount.toBigInteger());

            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, "deposit", params
            );
            handleContractError(response);
            return true;
        } catch (Exception e) {
            log.error("充值失败", e);
            throw new RuntimeException("充值失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean withdraw(BigDecimal amount) {
        try {
            CryptoKeyPair keyPair = getUserKeyPair();
            AssembleTransactionProcessor processor = getProcessor(keyPair);

            List<Object> params = new ArrayList<>();
            params.add(amount.toBigInteger());

            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, "withdraw", params
            );
            handleContractError(response);
            // 建议：这里最好插入一条 WITHDRAW 类型的 Transaction 记录
            return true;
        } catch (Exception e) {
            log.error("提现失败", e);
            throw new RuntimeException("提现失败，余额可能不足");
        }
    }

    // ==========================================
    // 私有辅助方法 (Helpers)
    // ==========================================

    /**
     * 核心方法：解决 N+1 问题，批量查询用户信息并填充到 VO
     */
    private List<NftVO> populateNftVOs(List<NftInfo> nftInfos) {
        if (nftInfos == null || nftInfos.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 收集所有涉及到的地址 (Owner 和 Creator)
        Set<String> addresses = nftInfos.stream()
                .map(NftInfo::getOwnerAddress)
                .collect(Collectors.toSet());
        addresses.addAll(nftInfos.stream()
                .map(NftInfo::getCreatorAddress)
                .collect(Collectors.toSet()));

        // 2. 批量查询用户表
        List<AppUser> users;
        if (!addresses.isEmpty()) {
            users = appUserMapper.selectList(
                    new LambdaQueryWrapper<AppUser>().in(AppUser::getBlockchainAddress, addresses)
            );
        } else {
            users = Collections.emptyList();
        }

        // 3. 构建 Map 方便查找 (Key: address, Value: AppUser)
        Map<String, AppUser> userMap = users.stream()
                .collect(Collectors.toMap(AppUser::getBlockchainAddress, Function.identity()));

        // 4. 组装 VO
        return nftInfos.stream().map(nft -> {
            NftVO vo = new NftVO();
            BeanUtils.copyProperties(nft, vo);

            // 填充 Owner
            if (userMap.containsKey(nft.getOwnerAddress())) {
                AppUser owner = userMap.get(nft.getOwnerAddress());
                vo.setOwnerId(owner.getUserId());
                vo.setOwnerName(owner.getUsername());
                vo.setOwnerAvatar(owner.getAvatarUrl());
                // vo.setOwnerAddress(nft.getOwnerAddress()); // 已经在 copyProperties 中复制
            }

            // 填充 Creator (如果 VO 有字段)
            if (userMap.containsKey(nft.getCreatorAddress())) {
                AppUser creator = userMap.get(nft.getCreatorAddress());
                vo.setCreatorName(creator.getUsername());
            }

            return vo;
        }).collect(Collectors.toList());
    }

    private Boolean executeContractAction(String nftId, String functionName, Object[] args, java.util.function.Consumer<NftInfo> dbUpdater) {
        NftInfo nftInfo = nftInfoMapper.selectById(nftId);
        if (nftInfo == null) throw new RuntimeException("NFT不存在");

        try {
            CryptoKeyPair keyPair = getUserKeyPair();
            AssembleTransactionProcessor processor = getProcessor(keyPair);

            List<Object> params = new ArrayList<>();
            params.add(new BigInteger(nftInfo.getTokenId()));
            if (args != null) {
                for (Object arg : args) params.add(arg);
            }

            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    CONTRACT_NAME, CONTRACT_ADDRESS, functionName, params
            );
            handleContractError(response);

            if (dbUpdater != null) {
                dbUpdater.accept(nftInfo);
                nftInfoMapper.updateById(nftInfo);
            }
            return true;
        } catch (Exception e) {
            log.error("执行合约方法 " + functionName + " 失败", e);
            throw new RuntimeException("操作失败: " + e.getMessage());
        }
    }

    private CryptoKeyPair getUserKeyPair() {
        String userId = userUtils.getCurrentUserId();
        CryptoKeyPair keyPair = appUserService.getCryptoKeyPairByUserId(userId);
        if(keyPair == null) {
            throw new RuntimeException("用户私钥加载失败");
        }
        return keyPair;
    }

    private AssembleTransactionProcessor getProcessor(CryptoKeyPair keyPair) {
        try {
            Client client = bcosSDK.getClient(GROUP_ID);
            return TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, ABI_PATH, BIN_PATH
            );
        } catch (Exception e) {
            throw new RuntimeException("初始化交易处理器失败", e);
        }
    }

    private AssembleTransactionProcessor getProcessor() {
        return getProcessor(getUserKeyPair());
    }

    private void handleContractError(TransactionResponse response) {
        if (!Objects.equals(response.getTransactionReceipt().getStatus(), "0") || !Objects.equals(response.getTransactionReceipt().getStatus(), "0x0")) {
            throw new RuntimeException("合约执行失败, Status: " + response.getTransactionReceipt().getStatus() +
                    ", Msg: " + response.getReceiptMessages());
        }
    }

    /**
     * 记录交易历史到数据库
     *
     * @param nftId 数据库中的 NFT主键 (UUID)
     * @param from  发送方钱包地址
     * @param to    接收方钱包地址
     * @param price 交易价格
     * @param txHash 链上交易哈希
     * @param status 交易状态 (1=成功)
     */
    private void recordTransaction(String nftId, String from, String to, BigDecimal price, String txHash, Integer status) {
        NftTransaction tx = new NftTransaction();
        tx.setNftId(nftId);
        tx.setTransactionHash(txHash);
        tx.setFromAddress(from);
        tx.setToAddress(to);
        tx.setPrice(price);
        tx.setStatus(status);
        tx.setCreateTime(LocalDateTime.now());
        nftTransactionMapper.insert(tx); // 存入自动生成主键
    }
}