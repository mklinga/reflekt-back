package com.mklinga.reflekt.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "entries")
@Setter
@Getter
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
}
