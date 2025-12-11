package moe.imtop1.imagehosting.common.service;

import moe.imtop1.imagehosting.common.vo.AdminDashboardStatsVO;

public interface StatsProvider {
    void fillStats(AdminDashboardStatsVO stats);
}
