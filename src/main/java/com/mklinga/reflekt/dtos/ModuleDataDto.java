package com.mklinga.reflekt.dtos;

import java.util.List;
import lombok.Data;

/**
 * Data transfer object for the ImageModule data.
 */
@Data
public class ModuleDataDto {
  private List<ImageModuleDataDto> images;
}
