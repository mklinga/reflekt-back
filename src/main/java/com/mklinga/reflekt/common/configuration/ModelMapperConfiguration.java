package com.mklinga.reflekt.common.configuration;

import com.mklinga.reflekt.journal.dtos.ImageDataDto;
import com.mklinga.reflekt.journal.model.Image;
import org.modelmapper.ModelMapper;

public class ModelMapperConfiguration {
  public static ModelMapper getModelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    /* Custom field mappings */

    modelMapper
        .typeMap(Image.class, ImageDataDto.class)
        .addMapping(Image::getImageName, ImageDataDto::setName);

    return modelMapper;
  }
}
