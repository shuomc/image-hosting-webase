package com.sjy.imagechain.service;

import java.math.BigDecimal;
import java.util.Map;

public interface NFTService {
    Map<String, Object> getNFTList(Integer page, Integer pageSize, String query);
    
    Map<String, Object> getMyNFTs(Integer page, Integer pageSize, String mode);
    
    Map<String, Object> mintNFT(String imageId, String description, BigDecimal price, String minioUrl);
    
    Map<String, Object> buyNFT(String nftId);
    
    Map<String, Object> setNFTPrice(String nftId, BigDecimal price);
    
    Map<String, Object> cancelNFTSale(String nftId);
    
    Map<String, Object> getNFTDetail(String nftId);
    
    Map<String, Object> getNFTTransactions(String nftId, Integer page, Integer pageSize);
} 