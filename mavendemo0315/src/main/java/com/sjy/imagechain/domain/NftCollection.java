package com.sjy.imagechain.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Accessors(chain = true)
@TableName("nft_collection")
@Schema(description = "NFT合集/系列表")
public class NftCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "合集ID")
    @TableId(value = "collection_id", type = IdType.AUTO)
    private Integer collectionId;

    @Schema(description = "系列名称")
    private String name;

    @Schema(description = "代币符号")
    private String symbol;

    @Schema(description = "智能合约地址")
    @TableField("contract_address")
    private String contractAddress;

    @Schema(description = "封面图URL")
    @TableField("cover_image")
    private String coverImage;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建者用户ID")
    @TableField("creator_user_id")
    private String creatorUserId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
