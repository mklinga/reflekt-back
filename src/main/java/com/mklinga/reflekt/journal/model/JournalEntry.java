package com.mklinga.reflekt.journal.model;

import com.mklinga.reflekt.authentication.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Database entity model for the JournalEntry.
 */
@Entity
@Table(name = "entries")
@Setter
@Getter
@NamedQueries({
    @NamedQuery(
        name = "JournalEntry_GetEntriesWithImages",
        query = "SELECT e.id FROM JournalEntry e"
            + " INNER JOIN Image m ON m.journalEntry = e"
            + " WHERE e.owner = :owner AND m.deleted = false")
})
@NamedNativeQueries(
    @NamedNativeQuery(
        name = "JournalEntry_GetNavigationData",
        query = "WITH e AS ("
            + " SELECT id,"
            + " lag(id) OVER (ORDER BY entry_date) AS previous,"
            + " lead(id) OVER (ORDER BY entry_date) AS next"
            + " FROM entries WHERE owner = :owner ORDER BY entry_date ASC)"
            + " SELECT cast(id as varchar),"
            + " cast(previous as varchar),"
            + " cast(next as varchar)"
            + " FROM e WHERE e.id = :id"
    )
)
public class JournalEntry {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "entry_date", nullable = false, columnDefinition = "DATE")
  private LocalDate entryDate;

  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
  private LocalDateTime updatedAt;

  @Column(name = "mood", nullable = false, columnDefinition = "TEXT")
  private String mood;

  @Column(name = "title", nullable = false, columnDefinition = "TEXT")
  private String title;

  @Column(name = "entry", nullable = false, columnDefinition = "TEXT")
  private String entry;

  @ManyToOne
  @JoinColumn(name = "owner")
  private User owner;

  @ManyToMany
  @OrderBy("color ASC, name ASC")
  @JoinTable(name = "tag_entries",
      joinColumns = @JoinColumn(name = "entry_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private List<Tag> tags;

  @OneToMany(mappedBy = "journalEntry")
  private Set<Image> images;

}
