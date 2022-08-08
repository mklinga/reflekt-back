package com.mklinga.reflekt.journal.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface RawJournalEntryResult {
  UUID getId();

  LocalDate getEntryDate();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

  String getMood();

  String getTitle();

  String getEntry();
}
