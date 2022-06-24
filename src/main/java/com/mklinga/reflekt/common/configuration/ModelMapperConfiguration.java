package com.mklinga.reflekt.common.configuration;

import com.mklinga.reflekt.journal.dtos.ImageDataDto;
import com.mklinga.reflekt.journal.model.Image;
import org.modelmapper.ModelMapper;

/**
 * We use a singleton ModelMapper throughout the application and we can define the specific rules
 * here for the variable namings etc. that the default operation does not work with.
 */
public class ModelMapperConfiguration {
  /**
   * Instantiates the modelMapper and sets the necessary rules.
   *
   * @return ModelMapper instance
   */
  public static ModelMapper getModelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    /* Custom field mappings */

    modelMapper
        .typeMap(Image.class, ImageDataDto.class)
        .addMapping(Image::getImageName, ImageDataDto::setName);

    return modelMapper;
  }
}
