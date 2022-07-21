package com.mklinga.reflekt.analytics.repositorios;


import com.mklinga.reflekt.analytics.model.JournalUserAnalytics;
import com.mklinga.reflekt.authentication.model.User;
import java.util.UUID;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JournalUserAnalyticsRepository extends CrudRepository<JournalUserAnalytics, UUID> {
  JournalUserAnalytics findJournalUserAnalyticsByUser(User user);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(value = "SELECT u FROM JournalUserAnalytics u WHERE u.user = :user")
  JournalUserAnalytics findJournalUserAnalyticsByUserWithLock(@Param("user") User user);

}
