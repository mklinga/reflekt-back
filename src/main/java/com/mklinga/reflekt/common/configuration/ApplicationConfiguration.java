package com.mklinga.reflekt.common.configuration;

import com.mklinga.reflekt.journal.dtos.ImageDataDto;
import com.mklinga.reflekt.journal.model.Image;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main configuration of the application.
 */
@Configuration
@EnableScheduling
public class ApplicationConfiguration {

  /**
   * Creates a single instance of modelmapper that is used throughout the application.
   *
   * @return modelMapper
   */
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    /* Custom field mappings */

    modelMapper
        .typeMap(Image.class, ImageDataDto.class)
        .addMapping(Image::getImageName, ImageDataDto::setName);

    return modelMapper;
  }
}
