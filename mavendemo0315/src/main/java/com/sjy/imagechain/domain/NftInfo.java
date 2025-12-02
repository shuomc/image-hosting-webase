package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjy.imagechain.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("nft_info")
@Schema(description = "NFT详情表")
public class NftInfo extends BaseEntity {

    @Schema(description = "系统内部NFT ID")
    @TableId(value = "nft_id", type = IdType.ASSIGN_ID)
    private String nftId;

    @Schema(description = "所属合集ID")
    @TableField("collection_id")
    private Integer collectionId;

    @Schema(description = "当前持有者地址")
    @TableField("owner_address")
    private String ownerAddress;

    @Schema(description = "创作者/铸造者地址")
    @TableField("creator_address")
    private String creatorAddress;

    @Schema(description = "链上Token ID")
    @TableField("token_id")
    private String tokenId;

    @Schema(description = "链上Metadata URI")
    @TableField("token_uri")
    private String tokenUri;

    @Schema(description = "合约地址")
    @TableField("contract_address")
    private String contractAddress;

    @Schema(description = "图片资源地址")
    @TableField("image_url")
    private String imageUrl;

    @Schema(description = "售价")
    private BigDecimal price;

    @Schema(description = "NFT名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "是否在售")
    @TableField("is_for_sale")
    private Boolean isForSale;
}