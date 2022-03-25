package com.mklinga.reflekt.dtos;

import com.mklinga.reflekt.model.modules.ImageModule;
import java.util.UUID;
import lombok.Data;

@Data
public class ImageModuleDataDto {
  private UUID id;
  private String name;

  public static ImageModuleDataDto of(ImageModule image) {
    ImageModuleDataDto imageModuleDataDto = new ImageModuleDataDto();
    imageModuleDataDto.setName(image.getImageName());
    imageModuleDataDto.setId(image.getId());

    return imageModuleDataDto;
  }
}
