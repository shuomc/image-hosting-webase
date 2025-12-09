package com.sjy.imagechain.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NftVO {
    private String nftId;           // 数据库主键
    private String tokenId;         // 链上TokenID

    private String name;            // NFT名称
    private String description;     // 描述
    private String imageUrl;        // MinIO 图片地址
    private BigDecimal price;       // 当前价格
    private Boolean isForSale;      // 是否在售

    // 用户信息 (通常显示名字和头像)
    private String ownerId;
    private String ownerName;
    private String ownerAvatar;
    private String ownerAddress;

    private String creatorName;     // 铸造者名字

    private String contractAddress; // 合约地址
    private String fileHash;        // 文件Hash
    private LocalDateTime createTime;
}
