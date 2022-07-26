package com.mklinga.reflekt.analytics.services;

import com.mklinga.reflekt.analytics.model.JournalUserAnalytics;
import com.mklinga.reflekt.analytics.repositorios.JournalUserAnalyticsRepository;
import com.mklinga.reflekt.authentication.model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AnalyticsService handles all things that are related to the analysing the user actions and data.
 */
@Service
public class AnalyticsService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @PersistenceContext
  private EntityManager entityManager;

  private final JournalUserAnalyticsRepository journalUserAnalyticsRepository;

  @Autowired
  public AnalyticsService(JournalUserAnalyticsRepository journalUserAnalyticsRepository) {
    this.journalUserAnalyticsRepository = journalUserAnalyticsRepository;
  }

  /**
   * Increase the analytic userUpdateCount in the database by 1. This method uses pessimistic
   * locking in the database to make sure every event is correctly handled.
   *
   * @param userId The ID of the user
   */
  @Transactional
  public void increaseUserUpdateCount(Integer userId) {
    User user = entityManager.find(User.class, userId);
    JournalUserAnalytics journalUserAnalytics = journalUserAnalyticsRepository
        .findJournalUserAnalyticsByUserWithLock(user);

    if (journalUserAnalytics == null) {
      journalUserAnalytics = new JournalUserAnalytics();
      journalUserAnalytics.setUser(user);
      journalUserAnalytics.setUpdateCount(0);
    }

    journalUserAnalytics.setUpdateCount(journalUserAnalytics.getUpdateCount() + 1);
    journalUserAnalyticsRepository.save(journalUserAnalytics);
  }

  /**
   * Retrieves the JournalUserAnalytics entity from the database for the specific user.
   *
   * @param user The user whose analytics we are fetching
   * @return JournalUserAnalytics object from the database
   */
  public JournalUserAnalytics getJournalUserAnalytics(User user) {
    return journalUserAnalyticsRepository.findJournalUserAnalyticsByUser(user);
  }
}
