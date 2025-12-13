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

    // Charts Data
    private java.util.List<String> uploadTrendDates;
    private java.util.List<Long> uploadTrendCounts;
    
    // Resource Usage (e.g. Image Types Distribution)
    private java.util.List<String> imageTypes;
    private java.util.List<Long> imageTypeCounts;

    // New Charts Data
    private Long mintedImageCount; // For Uploaded vs Minted
    private Long publicImageCount; // For Public vs Private
    private Long privateImageCount;
}
