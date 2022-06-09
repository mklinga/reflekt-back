package com.mklinga.reflekt.dtos;

import java.util.UUID;
import lombok.Data;

/**
 * Data transfer object for the search results.
 */
@Data
public class SearchResultDto {
  private UUID id;
  private String title;
  private String entry;
}
