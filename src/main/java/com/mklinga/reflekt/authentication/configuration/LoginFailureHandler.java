package com.mklinga.reflekt.authentication.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * Provider custom handling of the login failure flow. Instead of redirecting the user, we simply
 * return 401 Not authorised (the login is called through an AJAX call from the frontend,
 * so redirect doesn't make sense).
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) {

    logger.info("Invalid login attempt: " + exception.getMessage());
    response.setStatus(401);
  }
}
