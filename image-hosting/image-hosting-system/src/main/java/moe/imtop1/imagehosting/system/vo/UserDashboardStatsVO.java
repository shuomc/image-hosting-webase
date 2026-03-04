package moe.imtop1.imagehosting.system.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import moe.imtop1.imagehosting.system.domain.UserStats;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserDashboardStatsVO {
    // User Stats table fields
    private UserStats userStats;

    // Counts for charts
    private Long totalImages;
    private Long mintedCount;
    private Long unmintedCount;
    private Long publicCount;
    private Long privateCount;

    // NFT stats
    private Long totalNfts;

    // Transaction stats
    private Long transactionCount;
    private String walletBalance;

    // Transaction amount trend (for line chart)
    private List<String> transactionDates;
    private List<java.math.BigDecimal> transactionAmounts;
}
