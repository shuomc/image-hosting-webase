package learn.mavendemo0315.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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