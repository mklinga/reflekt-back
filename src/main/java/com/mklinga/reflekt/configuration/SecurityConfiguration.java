package com.mklinga.reflekt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  LoginSecurityHandler loginSecurityHandler;

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
        .successHandler(loginSecurityHandler);
  }
}
