package com.sjy.imagechain.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NftTransactionVO {
    private String transactionId;
    private String transactionHash; // 链上哈希

    private String nftName;         // 关联的NFT名称
    private String imageUrl;        // 图片预览

    private String fromAddress;     // 发送方
    private String toAddress;       // 接收方

    private BigDecimal price;       // 交易金额
    private Integer status;         // 0-打包中, 1-成功, 2-失败
    private String type;            // 交易类型: "MINT", "BUY", "TRANSFER"等

    private LocalDateTime createTime;
}
