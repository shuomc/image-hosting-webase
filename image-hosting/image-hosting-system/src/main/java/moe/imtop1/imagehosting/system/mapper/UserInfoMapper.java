package moe.imtop1.imagehosting.system.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper extends MPJBaseMapper<UserInfo> {

    /**
     * 修改密码
     */
    int setPassword(@Param("userEmail") String userEmail, @Param("newPassword") String newPassword);

    /**
     * 检查用户是否注册
     */
    boolean isRegistered(@Param("userEmail") String userEmail);

    /**
     * 增加已用空间 (原子操作，防止并发超卖)
     * @return 影响行数 (0表示空间不足或用户不存在)
     */
    @Update("UPDATE user_info SET storage_used = storage_used + #{size}, update_time = NOW() " +
            "WHERE user_id = #{userId} AND (storage_used + #{size}) <= storage_limit")
    int increaseStorage(@Param("userId") String userId, @Param("size") Long size);

    /**
     * 释放已用空间
     */
    @Update("UPDATE user_info SET storage_used = storage_used - #{size}, update_time = NOW() " +
            "WHERE user_id = #{userId} AND storage_used >= #{size}")
    int decreaseStorage(@Param("userId") String userId, @Param("size") Long size);
}