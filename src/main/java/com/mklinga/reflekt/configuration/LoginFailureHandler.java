package com.mklinga.reflekt.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) {

    response.setStatus(401);
  }
}
