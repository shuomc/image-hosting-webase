package moe.imtop1.imagehosting.images.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.imtop1.imagehosting.images.domain.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
    @Select("SELECT count(*) FROM favorites WHERE user_id = #{userId} AND image_id = #{imageId}")
    int countAll(@Param("userId") String userId, @Param("imageId") String imageId);

    @Update("UPDATE favorites SET is_delete = false WHERE user_id = #{userId} AND image_id = #{imageId}")
    int restoreFavorite(@Param("userId") String userId, @Param("imageId") String imageId);
}
