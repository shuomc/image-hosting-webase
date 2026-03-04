package moe.imtop1.imagehosting.system.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.imtop1.imagehosting.system.service.IUserStatsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 用户统计数据定时任务
 * @author shuomc
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UserStatsTask {

    private final IUserStatsService userStatsService;

    /**
     * 每小时刷新一次用户统计信息
     * cron 表达式: 0分0秒 每小时一次 (0 0 * * * *)
     */
    @Scheduled(cron = "0 0 * * * *")
    public void refreshUserStats() {
        log.info("开始执行用户统计数据刷新任务...");
        try {
            userStatsService.refreshAllUserStats();
            log.info("用户统计数据刷新任务完成。");
        } catch (Exception e) {
            log.error("执行用户统计数据刷新任务失败: ", e);
        }
    }
}
