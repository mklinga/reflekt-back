package com.mklinga.reflekt.configuration;

import com.mklinga.reflekt.dtos.ImageModuleDataDto;
import com.mklinga.reflekt.model.modules.ImageModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration of the application.
 */
@Configuration
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
        .typeMap(ImageModule.class, ImageModuleDataDto.class)
        .addMapping(ImageModule::getImageName, ImageModuleDataDto::setName);

    return modelMapper;
  }
}
