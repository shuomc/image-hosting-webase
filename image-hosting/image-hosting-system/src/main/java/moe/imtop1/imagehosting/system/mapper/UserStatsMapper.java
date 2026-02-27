package moe.imtop1.imagehosting.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.imtop1.imagehosting.system.domain.UserStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 用户统计 Mapper 接口
 * @author shuomc
 */
@Mapper
public interface UserStatsMapper extends BaseMapper<UserStats> {

    /**
     * 聚合 image_data 表中的数据并刷新到 user_stats 表
     * 使用 PostgreSQL 的 ON CONFLICT 语法进行合并更新
     * 使用 @InterceptorIgnore 忽略拦截器解析，因为 JSqlParser 不支持此复杂的 PostgreSQL ON CONFLICT 语法
     */
    @InterceptorIgnore(illegalSql = "true", blockAttack = "true")
    @Update("INSERT INTO user_stats (user_id, total_uploads, total_views, total_downloads, total_likes, storage_used, storage_limit, update_time) " +
            "SELECT " +
            "    user_id, " +
            "    COUNT(image_id)::int4 AS total_uploads, " +
            "    SUM(COALESCE(view_count, 0))::int8 AS total_views, " +
            "    SUM(COALESCE(download_count, 0))::int8 AS total_downloads, " +
            "    SUM(COALESCE(like_count, 0))::int8 AS total_likes, " +
            "    SUM(COALESCE(size, 0))::int8 AS storage_used, " +
            "    1073741824::int8 AS storage_limit, " +
            "    NOW() AS update_time " +
            "FROM " +
            "    image_data " +
            "WHERE " +
            "    is_delete = false " +
            "GROUP BY " +
            "    user_id " +
            "ON CONFLICT (user_id) DO UPDATE SET " +
            "    total_uploads = EXCLUDED.total_uploads, " +
            "    total_views = EXCLUDED.total_views, " +
            "    total_downloads = EXCLUDED.total_downloads, " +
            "    total_likes = EXCLUDED.total_likes, " +
            "    storage_used = EXCLUDED.storage_used, " +
            "    update_time = EXCLUDED.update_time")
    void refreshUserStats();

    @org.apache.ibatis.annotations.Select("SELECT COUNT(*) FROM image_data WHERE is_delete = false AND user_id = #{userId}")
    Long countTotalImagesByUserId(String userId);

    @org.apache.ibatis.annotations.Select("SELECT COUNT(*) FROM image_data WHERE is_delete = false AND nft_id IS NOT NULL AND user_id = #{userId}")
    Long countMintedByUserId(String userId);

    @org.apache.ibatis.annotations.Select("SELECT COUNT(*) FROM image_data WHERE is_delete = false AND is_public = true AND user_id = #{userId}")
    Long countPublicByUserId(String userId);

    @org.apache.ibatis.annotations.Select("SELECT COUNT(*) FROM image_data WHERE is_delete = false AND is_public = false AND user_id = #{userId}")
    Long countPrivateByUserId(String userId);
}
