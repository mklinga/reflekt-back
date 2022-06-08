package com.mklinga.reflekt.dtos;

import java.util.UUID;
import lombok.Data;

/**
 * Data transfer object for the image metadata.
 */
@Data
public class ImageModuleDataDto {
  private UUID id;
  private String name;
}
