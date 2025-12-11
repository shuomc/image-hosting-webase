package moe.imtop1.imagehosting.images.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import moe.imtop1.imagehosting.images.domain.ImageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ImageMapper extends MPJBaseMapper<ImageData> {
    @Select("SELECT COALESCE(SUM(size), 0) FROM image_data WHERE is_delete = false")
    Long sumSize();
}
