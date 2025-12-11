package moe.imtop1.imagehosting.nft.provider;

import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.common.service.StatsProvider;
import moe.imtop1.imagehosting.common.vo.AdminDashboardStatsVO;
import moe.imtop1.imagehosting.nft.service.INFTService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NftStatsProvider implements StatsProvider {

    private final INFTService nftService;

    @Override
    public void fillStats(AdminDashboardStatsVO stats) {
        // 4. NFT Transaction Volume
        try {
            AjaxResult nftResult = nftService.getTransactionStats("all");
            if (Integer.valueOf(200).equals(nftResult.get("code"))) {
                Object data = nftResult.get("data");
                if (data instanceof Map) {
                    Object total = ((Map<?, ?>) data).get("total");
                    if (total instanceof Number) {
                        stats.setNftTransactionVolume(((Number) total).longValue());
                    } else {
                         stats.setNftTransactionVolume(0L);
                    }
                } else {
                    stats.setNftTransactionVolume(0L);
                }
            } else {
                stats.setNftTransactionVolume(0L);
            }
        } catch (Exception e) {
            stats.setNftTransactionVolume(0L);
        }
    }
}
