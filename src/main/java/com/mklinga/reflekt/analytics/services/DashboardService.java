package com.mklinga.reflekt.analytics.services;

import com.mklinga.reflekt.analytics.dtos.DashboardDto;
import com.mklinga.reflekt.analytics.model.JournalUserAnalytics;
import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.services.JournalEntryService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
  private final AnalyticsService analyticsService;
  private final JournalEntryService journalEntryService;

  @Autowired
  public DashboardService(
      AnalyticsService analyticsService, JournalEntryService journalEntryService) {
    this.analyticsService = analyticsService;
    this.journalEntryService = journalEntryService;
  }

  public DashboardDto getDashboardData(User user) {
    JournalUserAnalytics journalUserAnalytics = analyticsService.getJournalUserAnalytics(user);
    Integer updateCount = journalUserAnalytics == null ? 0 : journalUserAnalytics.getUpdateCount();

    Integer entryCount = journalEntryService.getEntryCountByUser(user);

    DashboardDto data = new DashboardDto();
    data.setUpdateCount(updateCount);
    data.setEntryCount(entryCount);

    return data;
  }
}
