package moe.imtop1.imagehosting.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("nft_transaction")
public class NFTTransaction {
    private String transactionId;
    private String nftId;
    private String fromUserId;
    private String toUserId;
    private BigDecimal price;
    private String transactionHash;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isDelete;
} 