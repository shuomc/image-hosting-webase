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
@TableName("nft_transaction")
public class NFTTransaction {
    @TableId(type = IdType.ASSIGN_ID)
    private String transactionId;
    private String nftId;
    private String fromUserId;
    private String toUserId;
    private BigDecimal price;
    private String transactionHash;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Boolean isDelete;
} 