package com.mklinga.reflekt.analytics.services;

import com.mklinga.reflekt.analytics.model.JournalUserAnalytics;
import com.mklinga.reflekt.authentication.model.User;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    Query query = entityManager
        .createNamedQuery("lockAnalyticsJournalUser")
        .setParameter("user", user);

    JournalUserAnalytics journalUserAnalytics;

    try {
      journalUserAnalytics = (JournalUserAnalytics) query.getSingleResult();
    } catch (NoResultException noResultException) {
      journalUserAnalytics = new JournalUserAnalytics();
      journalUserAnalytics.setUser(user);
      journalUserAnalytics.setUpdateCount(0);
    }

    journalUserAnalytics.setUpdateCount(journalUserAnalytics.getUpdateCount() + 1);
    entityManager.persist(journalUserAnalytics);
  }
}
