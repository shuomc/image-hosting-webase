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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Accessors(chain = true)
@TableName("nft_transaction")
@Schema(description = "NFT交易记录表")
public class NftTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    // 交易状态常量
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 2;

    @Schema(description = "交易流水ID")
    @TableId(value = "transaction_id", type = IdType.ASSIGN_ID)
    private String transactionId;

    @Schema(description = "链上交易哈希")
    @TableField("transaction_hash")
    private String transactionHash;

    @Schema(description = "关联NFT ID")
    @TableField("nft_id")
    private String nftId;

    @Schema(description = "发送方地址")
    @TableField("from_address")
    private String fromAddress;

    @Schema(description = "接收方地址")
    @TableField("to_address")
    private String toAddress;

    @Schema(description = "交易金额")
    private BigDecimal price;

    @Schema(description = "状态: 0-打包中, 1-成功, 2-失败")
    private Integer status;

    @Schema(description = "交易类型: MINT, BUY, TRANSFER")
    private String type;

    @Schema(description = "区块高度")
    @TableField("block_number")
    private Long blockNumber;

    @Schema(description = "FISCO群组ID")
    @TableField("group_id")
    private Integer groupId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}