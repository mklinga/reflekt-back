package com.mklinga.reflekt.analytics.services;

import com.mklinga.reflekt.analytics.dtos.DashboardDto;
import com.mklinga.reflekt.analytics.model.JournalUserAnalytics;
import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.services.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DashboardService handles the necessary data fetching and combining for the Dashboard view.
 */
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

  /**
   * Returns the user-visible dashboard data. Data is combined from analytics and directly from the
   * entries repository.
   *
   * @param user The (authenticated) user whose data we are fetching
   * @return DashboardData in DTO format
   */
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
