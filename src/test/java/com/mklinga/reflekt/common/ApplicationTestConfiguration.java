package com.mklinga.reflekt.common;

import static com.mklinga.reflekt.common.TestAuthentication.testUser;

import com.mklinga.reflekt.authentication.configuration.LoginFailureHandler;
import com.mklinga.reflekt.authentication.configuration.LoginSuccessHandler;
import com.mklinga.reflekt.authentication.model.UserPrincipal;
import com.mklinga.reflekt.common.configuration.ModelMapperConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class ApplicationTestConfiguration {

  @MockBean
  private LoginSuccessHandler loginSuccessHandler;

  @MockBean
  private LoginFailureHandler loginFailureHandler;

  @Bean
  public ModelMapper modelMapper() {
    return ModelMapperConfiguration.getModelMapper();
  }

  @Bean
  public UserDetailsService testUserDetailsService() {
    return username -> new UserPrincipal(TestAuthentication.getUser(username));
  }
}
