package moe.imtop1.imagehosting.nft.service;

import moe.imtop1.imagehosting.common.dto.AjaxResult;
import java.math.BigDecimal;

public interface INFTService {

    // ==========================================
    // 1. 浏览与查询
    // ==========================================

    /**
     * NFT市场列表查询
     * @param page 页码
     * @param pageSize 每页大小
     * @param query 搜索关键词
     * @param category 分类
     * @param sort 排序 (new, price_asc, price_desc)
     */
    AjaxResult getNFTList(Integer page, Integer pageSize, String query, String category, String sort);

    /**
     * 获取我的NFT
     * @param mode 查询模式: "owned"(持有), "created"(铸造), "sold"(已卖出)
     */
    AjaxResult getMyNFTs(Integer page, Integer pageSize, String mode);

    /**
     * 获取NFT详情
     */
    AjaxResult getNFTDetail(String nftId);

    /**
     * 获取交易历史 (主要是"我的交易")
     * @param type 交易类型筛选
     */
    AjaxResult getNFTTransactions(Integer page, Integer pageSize, String type);

    /**
     * 获取所有交易历史
     */
    AjaxResult getAllNFTTransactions(Integer page, Integer pageSize, String type);

    AjaxResult getTransactionStats(String type);

    /**
     * 获取当前余额
     */
    AjaxResult getBalance();

    // ==========================================
    // 2. 核心交易
    // ==========================================

    /**
     * 铸造NFT
     * @param name NFT名称
     * @param collectionId 系列ID (可选)
     */
    AjaxResult mintNFT(String imageId, String thumbnailMinioUrl, String name, String description, BigDecimal price, Integer collectionId, String fileHash);

    /**
     * 购买NFT
     */
    AjaxResult buyNFT(String nftId);

    /**
     * 赠送/转移 NFT
     * @param toAddress 接收人钱包地址
     */
    AjaxResult transferNFT(String nftId, String toAddress);

    // ==========================================
    // 3. 商品管理
    // ==========================================

    /**
     * 设置NFT价格
     */
    AjaxResult setNFTPrice(String nftId, BigDecimal price);

    /**
     * 上架 (Put On Shelf)
     */
    AjaxResult putOnShelf(String nftId);

    /**
     * 下架 (Off Shelf) - 原 cancelNFTSale
     */
    AjaxResult offShelf(String nftId);

    // ==========================================
    // 4. 资金操作
    // ==========================================

    /**
     * 充值
     */
    AjaxResult deposit(BigDecimal amount);

    /**
     * 提现
     */
    AjaxResult withdraw(BigDecimal amount);

    // ==========================================
    // 5. Legacy / Webase 兼容接口
    // ==========================================

    AjaxResult getWebaseBalance();

    AjaxResult webaseDeposit(BigDecimal amount);

    AjaxResult getWebaseNFTInfo(String tokenId);

    AjaxResult getWebaseOwnedNFTs();

    // 注册
    AjaxResult registerUser(String userId, String userName, String userEmail, String passwordHash, String avatarUrl, String bio);

    // 检查用户是否已注册区块链账户 (返回 boolean 或具体地址)
    AjaxResult checkUserRegistration(String userId);

    AjaxResult deregisterUser(String userId);
}