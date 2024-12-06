package com.dziem.popapi.service;

import com.dziem.popapi.model.webpage.ActiveUsersStats;

import java.util.List;

public interface ActiveUsersPageService {
    List<ActiveUsersStats> getActiveUsersStatsThisWeek();
}
