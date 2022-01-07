package com.mklinga.reflekt.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class JournalListItemDto {
  private UUID id;

  private String mood;
  private String title;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate entryDate;
}
