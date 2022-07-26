package com.mklinga.reflekt.analytics.model;

import com.mklinga.reflekt.authentication.model.User;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * JournalUseAnalytics stores all the analytical information about the journal user.
 * TODO: Maybe we should move this into simple analytics_user, and use for CRM etc. as well?
 */
@Entity
@Table(name = "analytics_journal_user")
public class JournalUserAnalytics {
  @Id
  @Getter
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
