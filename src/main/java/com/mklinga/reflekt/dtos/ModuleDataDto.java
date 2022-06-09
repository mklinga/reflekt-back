package com.mklinga.reflekt.dtos;

import java.util.List;
import lombok.Data;

/**
 * Data transfer object for the Image data.
 */
@Data
public class ModuleDataDto {
  private List<ImageDataDto> images;
  private List<TagDataDto> tags;
}
