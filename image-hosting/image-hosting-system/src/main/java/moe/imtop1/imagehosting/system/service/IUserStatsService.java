package moe.imtop1.imagehosting.system.service;

import moe.imtop1.imagehosting.system.domain.UserStats;

/**
 * 用户统计服务接口
 * @author shuomc
 */
public interface IUserStatsService {

    /**
     * 获取指定用户的统计信息
     * @param userId 用户ID
     * @return 统计信息
     */
    UserStats selectUserStatsById(String userId);

    /**
     * 手动刷新所有用户统计信息
     */
    void refreshAllUserStats();

    /**
     * 获取用户数据总览
     * @param userId 用户ID
     * @return 数据总览VO
     */
    moe.imtop1.imagehosting.system.vo.UserDashboardStatsVO getUserDashboardStats(String userId);
}
