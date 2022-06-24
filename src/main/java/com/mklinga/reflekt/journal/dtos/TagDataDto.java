package com.mklinga.reflekt.journal.dtos;

import java.util.UUID;
import lombok.Data;

/**
 * Data transfer object for the Tags. Note that the id will be empty when the user is creating new
 * tag from the UI.
 */
@Data
public class TagDataDto {
  private UUID id;
  private String name;
  private String color;
}
