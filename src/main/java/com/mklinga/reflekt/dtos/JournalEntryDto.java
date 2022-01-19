package com.mklinga.reflekt.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

/**
 * Data transfer object of the JournalEntry.
 */
@Data
public class JournalEntryDto {
  private UUID id;

  private String mood;
  private String title;
  private String entry;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate entryDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]'Z'")
  private LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]'Z'")
  private LocalDateTime updatedAt;
}
