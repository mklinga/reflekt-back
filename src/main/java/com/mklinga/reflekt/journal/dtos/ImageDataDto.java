package com.mklinga.reflekt.journal.dtos;

import java.util.UUID;
import lombok.Data;

/**
 * Data transfer object for the image metadata.
 */
@Data
public class ImageDataDto {
  private UUID id;
  private String name;
}
