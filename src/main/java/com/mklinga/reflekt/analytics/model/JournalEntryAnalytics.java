package com.mklinga.reflekt.analytics.model;

import com.mklinga.reflekt.journal.model.JournalEntry;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * JournalEntryAnalytics handles all the analytics for specific journal entry in the database.
 */
@Entity
@Table(name = "analytics_journal_entry")
public class JournalEntryAnalytics {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "entry_id", referencedColumnName = "id")
  private JournalEntry journalEntry;

  @Column(name = "word_count")
  private Integer wordCount;
}
