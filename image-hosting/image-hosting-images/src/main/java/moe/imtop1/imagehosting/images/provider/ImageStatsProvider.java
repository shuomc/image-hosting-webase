package moe.imtop1.imagehosting.images.provider;

import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.common.service.StatsProvider;
import moe.imtop1.imagehosting.common.utils.FileUtil;
import moe.imtop1.imagehosting.common.vo.AdminDashboardStatsVO;
import moe.imtop1.imagehosting.images.service.ImageService;
import org.springframework.stereotype.Component;

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
    }
}
