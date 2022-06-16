package com.mklinga.reflekt.common.configuration;

import com.mklinga.reflekt.authentication.configuration.LoginFailureHandler;
import com.mklinga.reflekt.authentication.configuration.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Provides the configuration for security- related items of the application, such as the
 * authentication methods and used password encoder.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  LoginSuccessHandler loginSuccessHandler;

  @Autowired
  LoginFailureHandler loginFailureHandler;

  @Override
  protected void configure(HttpSecurity security) throws Exception {
    security
        .csrf().disable()
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .loginProcessingUrl("/login")
        .usernameParameter("user")
        .passwordParameter("password")
        .successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler);
  }
}