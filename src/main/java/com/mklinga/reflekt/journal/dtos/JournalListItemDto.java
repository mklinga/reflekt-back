package com.mklinga.reflekt.journal.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/**
 * Provides the data transfer object for the items in the JournalEntries list
 * (this Dto should always be a subset of the JournalEntryDto).
 */
@Data
public class JournalListItemDto {
  private UUID id;

  private String mood;
  private String title;

  private boolean hasImages;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate entryDate;

  private List<TagDataDto> tags;
}
