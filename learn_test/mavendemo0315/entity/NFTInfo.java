package learn.mavendemo0315.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NFTInfo {
    private String nftId;
    private String imageId;
    private String ownerId;
    private String tokenId;
    private String contractAddress;
    private BigDecimal price;
    private Boolean isForSale;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isDelete;
} 