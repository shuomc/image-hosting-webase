package moe.imtop1.imagehosting.system.service;

import moe.imtop1.imagehosting.common.dto.AjaxResult;
import java.math.BigDecimal;

public interface NFTService {
    // NFT列表查询
    AjaxResult getNFTList(Integer page, Integer pageSize, String query);
    
    // 获取我的NFT
    AjaxResult getMyNFTs(Integer page, Integer pageSize);
    
    // 铸造NFT
    AjaxResult mintNFT(String imageId, String description, BigDecimal price, String minioUrl);
    
    // 购买NFT
    AjaxResult buyNFT(String nftId);
    
    // 设置NFT价格
    AjaxResult setNFTPrice(String nftId, BigDecimal price);
    
    // 取消NFT销售
    AjaxResult cancelNFTSale(String nftId);
    
    // 获取NFT详情
    AjaxResult getNFTDetail(String nftId);
    
    // 获取NFT交易历史
    AjaxResult getNFTTransactions();
    
    // 获取余额
    AjaxResult getBalance();
    
    // 充值
    AjaxResult deposit(BigDecimal amount);

    // Webase接口
    AjaxResult getWebaseBalance();
    
    AjaxResult webaseDeposit(BigDecimal amount);
    
    AjaxResult getWebaseNFTInfo(String tokenId);
    
    AjaxResult getWebaseOwnedNFTs();

} 