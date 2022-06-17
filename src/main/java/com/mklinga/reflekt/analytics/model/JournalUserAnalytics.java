package com.mklinga.reflekt.analytics.model;

import com.mklinga.reflekt.authentication.model.User;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.LockModeType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NamedQuery(
    name="GetJournalUserAnalyticsByUserWithLock",
    query="SELECT u FROM JournalUserAnalytics u WHERE u.user = :user",
    lockMode = LockModeType.PESSIMISTIC_WRITE)
@NamedQuery(
    name="GetJournalUserAnalyticsByUser",
    query="SELECT u FROM JournalUserAnalytics u WHERE u.user = :user"
)
@Entity
@Table(name = "analytics_journal_user")
public class JournalUserAnalytics {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @OneToOne
  @Setter
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(name = "update_count")
  @Getter
  @Setter
  private Integer updateCount;
}
