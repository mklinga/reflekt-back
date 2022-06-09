package com.mklinga.reflekt.model;

import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Tag entity can be set to one or more journal entries and can be used to group similar entries
 * together. Tags are dynamic, and each tag must have exactly one owner.
 */
@Entity
@Table(name = "tags")
@Getter
public class Tag {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  @Setter
  private UUID id;

  @Column(name = "name", nullable = false)
  @Setter
  private String name;

  @Column(name = "color")
  @Setter
  private String color;

  @ManyToOne
  @Setter
  @JoinColumn(name = "owner", nullable = false)
  private User owner;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "tag_entries",
      joinColumns = @JoinColumn(name = "tag_id"),
      inverseJoinColumns = @JoinColumn(name = "entry_id"))
  private List<JournalEntry> journalEntry;
}
