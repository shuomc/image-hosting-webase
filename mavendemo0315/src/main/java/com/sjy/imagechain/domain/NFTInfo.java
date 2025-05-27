package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@TableName("nft_info")
public class NFTInfo {
    @TableId(type = IdType.ASSIGN_ID)
    private String nftId;
    private String imageId;
    private String minioUrl;
    private String blockchainAddress;
    private String tokenId;
    private String contractAddress;
    private String description;
    private BigDecimal price;
    private Boolean isForSale;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Boolean isDelete;

}