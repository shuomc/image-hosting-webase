package com.sjy.imagechain.service;

import com.sjy.imagechain.domain.NftInfo;
import com.sjy.imagechain.domain.vo.NftTransactionVO;
import com.sjy.imagechain.domain.vo.NftVO;
import com.sjy.imagechain.domain.vo.PageData;
import com.sjy.imagechain.domain.vo.TransactionStatsVO;

import java.math.BigDecimal;
import java.util.List;

public interface NftService {

    // ==========================================
    // 1. 浏览与查询
    // ==========================================

    /**
     * 获取NFT市场列表
     * @param page 当前页码
     * @param pageSize 每页大小
     * @param query 搜索关键词
     * @param category 分类
     * @param sort 排序方式
     * @return 分页数据对象 (List<NftVO> + total)
     */
    PageData<NftVO> getNFTList(Integer page, Integer pageSize, String query, String category, String sort);

    /**
     * 获取NFT详情
     * @param nftId 系统NFT ID
     * @return NftVO 详情对象
     */
    NftVO getNFTDetail(String nftId);

    /**
     * 获取我的 NFT
     * @param page 页码
     * @param pageSize 大小
     * @param mode 模式: "owned"(持有), "created"(铸造), "sold"(已卖)
     * @return 分页数据
     */
    PageData<NftVO> getMyNFTs(Integer page, Integer pageSize, String mode);

    /**
     * 获取我的交易历史
     */
    PageData<NftTransactionVO> getMyTransactions(Integer page, Integer pageSize, String type);

    /**
     * 获取所有交易历史 (管理员/市场浏览器)
     */
    PageData<NftTransactionVO> getAllTransactions(Integer page, Integer pageSize, String type);

    /**
     * 获取交易统计数据
     */
    Object getTransactionStats(String type);

    /**
     * 获取用户余额
     * @return 余额数值
     */
    BigDecimal getBalance();

    // ==========================================
    // 2. 核心交易 (Write Operations)
    // ==========================================

    /**
     * 铸造 NFT
     * @param imageId 图片Id
     * @param thumbnailMinioUrl 图片缩略图url
     * @param name 名称
     * @param description 描述
     * @param price 初始价格
     * @param collectionId 集合ID
     * @return 生成的 nftId
     */
    NftInfo mintNFT(String imageId, String thumbnailMinioUrl, String name, String description, BigDecimal price, Integer collectionId, String fileHash);

    /**
     * 购买 NFT
     * @param nftId NFT ID
     * @return 是否成功提交交易
     */
    Boolean buyNFT(String nftId);

    /**
     * 赠送 NFT
     * @param nftId NFT ID
     * @param toAddress 目标地址
     * @return 是否成功
     */
    Boolean transferNFT(String nftId, String toAddress);

    // ==========================================
    // 3. 状态与价格管理 (匹配 setForSale 逻辑)
    // ==========================================

    /**
     * 修改价格 (对应合约 updatePrice)
     * @param nftId NFT ID
     * @param newPrice 新价格
     * @return 是否成功
     */
    Boolean updateNFTPrice(String nftId, BigDecimal newPrice);

    /**
     * 上架 (对应合约 setForSale(tokenId, true))
     * 注意：通常上架前需要确保价格已设置正确，或者可以在Controller层组合调用 updatePrice + putOnShelf
     * @param nftId NFT ID
     * @return 是否成功
     */
    Boolean putOnShelf(String nftId);

    /**
     * 下架 (对应合约 setForSale(tokenId, false))
     * @param nftId NFT ID
     * @return 是否成功
     */
    Boolean offShelf(String nftId);

    /**
     * 系统级下架 (用于管理员删除图片时自动下架)
     * 使用NFT所有者的密钥执行操作，不需要当前用户认证
     * @param nftId NFT ID
     * @return 是否成功
     */
    Boolean systemOffShelf(String nftId);

    // ==========================================
    // 4. 资金管理
    // ==========================================

    /**
     * 充值
     */
    Boolean deposit(BigDecimal amount);

    /**
     * 提现
     */
    Boolean withdraw(BigDecimal amount);

    /**
     * 根据图片ID删除NFT信息 (逻辑删除)
     * @param imageId 图片ID
     */
    void deleteByImageId(String imageId);
}