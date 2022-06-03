package com.mklinga.reflekt.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Provider custom handling of the login success flow. Instead of redirecting the user, we simply
 * return 200 OK (the login is called through an AJAX call from the frontend, so redirect doesn't
 * make sense).
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) {
    logger.info("User \"" + authentication.getName() + "\" has successfully logged in.");
    response.setStatus(200);
  }
}
