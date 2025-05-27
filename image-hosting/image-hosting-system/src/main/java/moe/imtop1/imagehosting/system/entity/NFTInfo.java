package moe.imtop1.imagehosting.system.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class NFTInfo {
    private String nftId;
    private String imageId;
    private String minioUrl;
    private String ownerId;
    private String tokenId;
    private String contractAddress;
    private BigDecimal price;
    private String description;
    private Boolean isForSale;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isDelete;
} 