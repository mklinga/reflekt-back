package com.mklinga.reflekt.contacts.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Table(name = "contact_events")
public class ContactEvent {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "event_type", nullable = false)
  private ContactEventType eventType;

  @Column(name = "event_date", nullable = false, columnDefinition = "DATE")
  private LocalDate eventDate;

  @Column(name = "title", columnDefinition = "TEXT", nullable = false)
  private String title;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "contact_event_participants",
      joinColumns = @JoinColumn(name = "contact_event_id"),
      inverseJoinColumns = @JoinColumn(name = "contact_id"))
  private Set<JpaContact> participants;
}
