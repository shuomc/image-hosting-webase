package moe.imtop1.imagehosting.images.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import moe.imtop1.imagehosting.images.domain.ImageData;
import moe.imtop1.imagehosting.images.domain.dto.StatsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import java.util.List;
import java.util.Map;

@Mapper
public interface ImageMapper extends MPJBaseMapper<ImageData> {
    @Select("SELECT COALESCE(SUM(size), 0) FROM image_data WHERE is_delete = false")
    Long sumSize();

    @InterceptorIgnore(illegalSql = "true")
    @Select("SELECT TO_CHAR(create_time, 'YYYY-MM-DD') as key, COUNT(*) as count FROM image_data WHERE is_delete = false AND create_time >= NOW() - INTERVAL '7 days' GROUP BY key ORDER BY key")
    List<StatsDTO> getUploadTrend();

    @Select("SELECT content_type as key, COUNT(*) as count FROM image_data WHERE is_delete = false GROUP BY content_type")
    List<StatsDTO> getImageTypeDistribution();

    @Select("SELECT COUNT(*) FROM image_data WHERE is_delete = false AND nft_id IS NOT NULL")
    Long countMinted();

    @Select("SELECT COUNT(*) FROM image_data WHERE is_delete = false AND is_public = true")
    Long countPublic();

    @Select("SELECT COUNT(*) FROM image_data WHERE is_delete = false AND is_public = false")
    Long countPrivate();
}
