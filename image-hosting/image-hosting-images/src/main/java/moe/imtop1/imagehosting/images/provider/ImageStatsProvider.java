package moe.imtop1.imagehosting.images.provider;

import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.common.service.StatsProvider;
import moe.imtop1.imagehosting.common.utils.FileUtil;
import moe.imtop1.imagehosting.common.vo.AdminDashboardStatsVO;
import moe.imtop1.imagehosting.images.domain.dto.StatsDTO;
import moe.imtop1.imagehosting.images.service.ImageService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageStatsProvider implements StatsProvider {

    private final ImageService imageService;

    @Override
    public void fillStats(AdminDashboardStatsVO stats) {
        // 2. Total Images
        stats.setTotalImages(imageService.count());

        // 3. Storage Usage
        Long totalSize = imageService.getTotalStorageUsage();
        stats.setStorageUsed(FileUtil.formatFileSize(totalSize != null ? totalSize : 0));

        // 5. Upload Trend
        List<StatsDTO> trend = imageService.getUploadTrend();
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        if (trend != null) {
            for (StatsDTO entry : trend) {
                if (entry == null) continue;
                dates.add(entry.getKey());
                counts.add(entry.getCount() != null ? entry.getCount() : 0L);
            }
        }
        stats.setUploadTrendDates(dates);
        stats.setUploadTrendCounts(counts);

        // 6. Image Type Distribution
        List<StatsDTO> types = imageService.getImageTypeDistribution();
        List<String> typeNames = new ArrayList<>();
        List<Long> typeCounts = new ArrayList<>();
        if (types != null) {
            for (StatsDTO entry : types) {
                if (entry == null) continue;
                typeNames.add(entry.getKey());
                typeCounts.add(entry.getCount() != null ? entry.getCount() : 0L);
            }
        }
        stats.setImageTypes(typeNames);
        stats.setImageTypeCounts(typeCounts);

        // 7. New Stats for Charts
        stats.setMintedImageCount(imageService.getMintedCount());
        stats.setPublicImageCount(imageService.getPublicCount());
        stats.setPrivateImageCount(imageService.getPrivateCount());
    }
}
