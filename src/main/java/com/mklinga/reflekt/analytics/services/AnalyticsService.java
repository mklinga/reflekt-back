package com.mklinga.reflekt.analytics.services;

import com.mklinga.reflekt.analytics.model.JournalUserAnalytics;
import com.mklinga.reflekt.authentication.model.User;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalyticsService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void increaseUserUpdateCount(Integer userId) {
    User user = entityManager.find(User.class, userId);
    TypedQuery<JournalUserAnalytics> query = entityManager
        .createNamedQuery("GetJournalUserAnalyticsByUserWithLock", JournalUserAnalytics.class)
        .setParameter("user", user);

    JournalUserAnalytics journalUserAnalytics;

    try {
      journalUserAnalytics = query.getSingleResult();
    } catch (NoResultException noResultException) {
      journalUserAnalytics = new JournalUserAnalytics();
      journalUserAnalytics.setUser(user);
      journalUserAnalytics.setUpdateCount(0);
    }

    journalUserAnalytics.setUpdateCount(journalUserAnalytics.getUpdateCount() + 1);
    entityManager.persist(journalUserAnalytics);
  }

  public JournalUserAnalytics getJournalUserAnalytics(User user) {
    TypedQuery<JournalUserAnalytics> query = entityManager
        .createNamedQuery("GetJournalUserAnalyticsByUser", JournalUserAnalytics.class)
        .setParameter("user", user);

    try {
      return query.getSingleResult();
    } catch (NoResultException noResultException) {
      return null;
    } catch (Exception e) {
      logger.error("Something has gone wrong with getJournalUserAnalytics");
      logger.error(e.getMessage());
      return null;
    }
  }
}
