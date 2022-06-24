package com.mklinga.reflekt.common.configuration;

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
    return ModelMapperConfiguration.getModelMapper();
  }
}
