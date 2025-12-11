package moe.imtop1.imagehosting.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AdminDashboardStatsVO {
    private Long totalUsers;
    private Long totalImages;
    private String storageUsed; // Formatted string like "1.2 TB"
    private Long nftTransactionVolume;
}
