//package learn.mavendemo0315.service.impl;
//
//import learn.mavendemo0315.entity.NFTInfo;
//import learn.mavendemo0315.entity.NFTTransaction;
//import learn.mavendemo0315.service.NFTService;
//import org.fisco.bcos.sdk.BcosSDK;
//import org.fisco.bcos.sdk.client.Client;
//import org.fisco.bcos.sdk.config.Config;
//import org.fisco.bcos.sdk.config.ConfigOption;
//import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
//import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
//import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
//import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.*;
//
//@Service
//public class NFTServiceImpl implements NFTService {
//
//    @Autowired
//    private BcosSDK bcosSDK;
//
//    private static final String CONTRACT_ADDRESS = "0x7b9558d415a181859c0467653bc27cbb7b8799f6"; // 替换为实际部署的合约地址
//
//    @Override
//    public Map<String, Object> getNFTList(Integer page, Integer pageSize, String query) {
//        // TODO: 实现从数据库获取NFT列表
//        return null;
//    }
//
//    @Override
//    public Map<String, Object> getMyNFTs(Integer page, Integer pageSize, String mode) {
//        // TODO: 实现从数据库获取我的NFT列表
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public Map<String, Object> mintNFT(String imageId, String description, BigDecimal price) {
//        try {
//            // 获取客户端
//            Client client = bcosSDK.getClient(1);
//            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
//            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
//                client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
//            );
//
//            // 调用智能合约铸造NFT
//            List<Object> params = new ArrayList<>();
//            params.add(keyPair.getAddress()); // 接收者地址
//            params.add(description); // NFT描述
//            params.add(price.toBigInteger()); // 价格
//
//            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
//                "ImageNFT", CONTRACT_ADDRESS, "mintNFT", params
//            );
//
//            // 保存NFT信息到数据库
//            NFTInfo nftInfo = new NFTInfo();
//            nftInfo.setNftId(UUID.randomUUID().toString());
//            nftInfo.setImageId(imageId);
//            nftInfo.setOwnerId(keyPair.getAddress());
//            nftInfo.setTokenId(response.getReturnObject().get(0).toString());
//            nftInfo.setContractAddress(CONTRACT_ADDRESS);
//            nftInfo.setPrice(price);
//            nftInfo.setIsForSale(false);
//            nftInfo.setCreateTime(new Date());
//            nftInfo.setIsDelete(false);
//
//            // TODO: 保存到数据库
//
//            return Map.of(
//                "nftId", nftInfo.getNftId(),
//                "tokenId", nftInfo.getTokenId(),
//                "transactionHash", response.getTransactionReceipt().getTransactionHash()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("铸造NFT失败", e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public Map<String, Object> buyNFT(String nftId) {
//        try {
//            // 获取NFT信息
//            NFTInfo nftInfo = getNFTInfo(nftId);
//            if (nftInfo == null || !nftInfo.getIsForSale()) {
//                throw new RuntimeException("NFT不存在或未在售");
//            }
//
//            // 获取客户端
//            Client client = bcosSDK.getClient(1);
//            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
//            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
//                client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
//            );
//
//            // 调用智能合约购买NFT
//            List<Object> params = new ArrayList<>();
//            params.add(nftInfo.getTokenId());
//
//            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
//                "ImageNFT", CONTRACT_ADDRESS, "buyNFT", params
//            );
//
//            // 保存交易记录
//            NFTTransaction transaction = new NFTTransaction();
//            transaction.setTransactionId(UUID.randomUUID().toString());
//            transaction.setNftId(nftId);
//            transaction.setFromUserId(nftInfo.getOwnerId());
//            transaction.setToUserId(keyPair.getAddress());
//            transaction.setPrice(nftInfo.getPrice());
//            transaction.setTransactionHash(response.getTransactionReceipt().getTransactionHash());
//            transaction.setCreateTime(new Date());
//            transaction.setIsDelete(false);
//
//            // TODO: 保存交易记录到数据库
//
//            // 更新NFT所有者
//            nftInfo.setOwnerId(keyPair.getAddress());
//            nftInfo.setIsForSale(false);
//            nftInfo.setUpdateTime(new Date());
//
//            // TODO: 更新数据库中的NFT信息
//
//            return Map.of(
//                "transactionId", transaction.getTransactionId(),
//                "transactionHash", transaction.getTransactionHash()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("购买NFT失败", e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public Map<String, Object> setNFTPrice(String nftId, BigDecimal price) {
//        try {
//            // 获取NFT信息
//            NFTInfo nftInfo = getNFTInfo(nftId);
//            if (nftInfo == null) {
//                throw new RuntimeException("NFT不存在");
//            }
//
//            // 获取客户端
//            Client client = bcosSDK.getClient(1);
//            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
//            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
//                client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
//            );
//
//            // 调用智能合约设置价格
//            List<Object> params = new ArrayList<>();
//            params.add(nftInfo.getTokenId());
//            params.add(price.toBigInteger());
//
//            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
//                "ImageNFT", CONTRACT_ADDRESS, "updatePrice", params
//            );
//
//            // 更新NFT价格
//            nftInfo.setPrice(price);
//            nftInfo.setIsForSale(true);
//            nftInfo.setUpdateTime(new Date());
//
//            // TODO: 更新数据库中的NFT信息
//
//            return Map.of(
//                "nftId", nftId,
//                "price", price,
//                "transactionHash", response.getTransactionReceipt().getTransactionHash()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("设置NFT价格失败", e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public Map<String, Object> cancelNFTSale(String nftId) {
//        try {
//            // 获取NFT信息
//            NFTInfo nftInfo = getNFTInfo(nftId);
//            if (nftInfo == null) {
//                throw new RuntimeException("NFT不存在");
//            }
//
//            // 获取客户端
//            Client client = bcosSDK.getClient(1);
//            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
//            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
//                client, keyPair, "src/main/resources/abi/", "src/main/resources/bin/"
//            );
//
//            // 调用智能合约取消出售
//            List<Object> params = new ArrayList<>();
//            params.add(nftInfo.getTokenId());
//            params.add(false);
//
//            TransactionResponse response = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
//                "ImageNFT", CONTRACT_ADDRESS, "setForSale", params
//            );
//
//            // 更新NFT状态
//            nftInfo.setIsForSale(false);
//            nftInfo.setUpdateTime(new Date());
//
//            // TODO: 更新数据库中的NFT信息
//
//            return Map.of(
//                "nftId", nftId,
//                "transactionHash", response.getTransactionReceipt().getTransactionHash()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("取消NFT出售失败", e);
//        }
//    }
//
//    @Override
//    public Map<String, Object> getNFTDetail(String nftId) {
//        // TODO: 实现从数据库获取NFT详情
//        return null;
//    }
//
//    @Override
//    public Map<String, Object> getNFTTransactions(String nftId, Integer page, Integer pageSize) {
//        // TODO: 实现从数据库获取NFT交易历史
//        return null;
//    }
//
//    private NFTInfo getNFTInfo(String nftId) {
//        // TODO: 实现从数据库获取NFT信息
//        return null;
//    }
//}