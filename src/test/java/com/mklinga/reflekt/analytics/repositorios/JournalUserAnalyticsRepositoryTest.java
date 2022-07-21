package com.mklinga.reflekt.analytics.repositorios;

import static com.mklinga.reflekt.common.TestAuthentication.testUser;
import static org.junit.jupiter.api.Assertions.*;

import com.mklinga.reflekt.analytics.model.JournalUserAnalytics;
import com.mklinga.reflekt.authentication.model.User;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/test-scripts/analytics/AnalyticsServiceTestSql.sql")
class JournalUserAnalyticsRepositoryTest {

  @Autowired
  private JournalUserAnalyticsRepository journalUserAnalyticsRepository;

  @Nested
  @DisplayName("findJournalUserAnalyticsByUserWithLock")
  public class FindJournalUserAanalyticsByUserWithLockTests {

    @Test
    void findsCorrectUser1() {
      User testUser = testUser();
      JournalUserAnalytics journalUserAnalytics = journalUserAnalyticsRepository
          .findJournalUserAnalyticsByUserWithLock(testUser);

      assertNotNull(journalUserAnalytics);
      assertEquals(journalUserAnalytics.getId().toString(), "b98a688e-6bc3-4ae9-abde-df53f192f9bc");
      assertEquals(journalUserAnalytics.getUpdateCount(), 100);
    }

    @Test
    void findsCorrectUser2() {
      User testUser2 = testUser();
      testUser2.setId(2);
      JournalUserAnalytics journalUserAnalytics = journalUserAnalyticsRepository
          .findJournalUserAnalyticsByUserWithLock(testUser2);

      assertNotNull(journalUserAnalytics);
      assertEquals(journalUserAnalytics.getId().toString(), "cce999a6-e625-4c1b-84c4-018ed240e0d6");
      assertEquals(journalUserAnalytics.getUpdateCount(), 5);
    }
  }
}