package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.HelloDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides the endpoint used to check whether the user is authenticated.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
  private String serverName = "Friendly Java server";

  /**
   * This endpoint is called from the frontend to verify the user is authenticated.
   *
   * @param user When user session is authenticated, this contains the user principal.
   * @return Returns a simple string, containing the authenticated username.
   */
  @GetMapping
  public ResponseEntity<HelloDto> sayHello(@AuthenticationPrincipal User user) {
    if (user == null) {
      return ResponseEntity.status(401).build();
    }

    HelloDto hello = new HelloDto(serverName + " - Logged in as " + user.getUsername());
    return ResponseEntity.ok(hello);
  }
}
