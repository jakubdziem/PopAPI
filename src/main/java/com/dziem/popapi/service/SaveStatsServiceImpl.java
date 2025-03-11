package com.dziem.popapi.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveStatsServiceImpl implements SaveStatsService {
    private static final Logger logger = LoggerFactory.getLogger(StatsPageServiceImpl.class);
    private final ActiveUsersPageService activeUsersPageService;
    private final StatsPageChartService statsPageChartService;
    private final StatsPageService statsPageService;
    @Override
    @Scheduled(cron = "0 0 6 * * SUN", zone = "Europe/Warsaw")
    public void saveWeeklySnapshots() {
        logger.info("Starting weekly stats snapshot...");
        statsPageService.saveWeeklyStatsSnapshot();
        logger.info("Weekly stats snapshot completed.");
        logger.info("Starting weekly new users summed snapshot...");
        statsPageChartService.saveWeeklyNewUsersSummed();
        logger.info("Weekly new users summed snapshot completed.");
        logger.info("Starting weekly active users summed snapshot...");
        statsPageChartService.saveWeeklyActiveUsers();
        logger.info("Weekly active users summed completed.");
        logger.info("Starting weekly active users stats snapshot...");
        activeUsersPageService.saveWeeklyActiveUsersStatsSnapshot();
        logger.info("Weekly active users stats snapshot completed.");
    }

    @Override
    @Scheduled(cron = "0 30 23 * * *", zone = "Europe/Warsaw")
    public void saveDailySnapshots() {
        logger.info("Starting daily summed stats snapshot...");
        statsPageChartService.saveDailyStatsSummedSnapshot();
        logger.info("Daily summed stats snapshot completed.");
        logger.info("Starting daily summed users snapshot...");
        statsPageChartService.saveDailySummedUsersSnapshot();
        logger.info("Daily summed users snapshot completed.");
        logger.info("Starting daily active users snapshot...");
        statsPageChartService.saveDailyActiveUsers();
        logger.info("Daily active users snapshot completed.");
    }
}
