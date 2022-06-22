package com.mklinga.reflekt.common;

import com.mklinga.reflekt.authentication.configuration.LoginFailureHandler;
import com.mklinga.reflekt.authentication.configuration.LoginSuccessHandler;
import com.mklinga.reflekt.authentication.model.Role;
import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.authentication.model.UserPrincipal;
import com.mklinga.reflekt.common.configuration.ModelMapperConfiguration;
import java.util.List;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

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

  public static User testUser() {
    User user = new User();
    user.setUsername("test-user");
    user.setPassword("abc");

    Role role = new Role();
    role.setRole("USER");

    user.setRoles(Set.of(role));

    return user;
  }

  @Bean
  public UserDetailsService testUserDetailsService() {

    UserPrincipal userDetails = new UserPrincipal(testUser());

    return new UserDetailsManager() {

      private final InMemoryUserDetailsManager inMemoryUserDetailsManager =
          new InMemoryUserDetailsManager(List.of(userDetails));

      @Override
      public void createUser(UserDetails userDetails) {
        this.inMemoryUserDetailsManager.createUser(userDetails);
      }

      @Override
      public void updateUser(UserDetails userDetails) {
        this.inMemoryUserDetailsManager.updateUser(userDetails);
      }

      @Override
      public void deleteUser(String s) {
        this.inMemoryUserDetailsManager.deleteUser(s);
      }

      @Override
      public void changePassword(String s, String s1) {
        this.inMemoryUserDetailsManager.changePassword(s, s1);
      }

      @Override
      public boolean userExists(String s) {
        return this.inMemoryUserDetailsManager.userExists(s);
      }

      @Override
      public UserDetails loadUserByUsername(String userName) {
        return new UserPrincipal(testUser());
      }
    };
  }
}
