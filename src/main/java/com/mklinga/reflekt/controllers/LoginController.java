package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.HelloDto;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello Controller, which politely greets the clients.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
  private String serverName = "Friendly Java server";

  @GetMapping
  public ResponseEntity<HelloDto> sayHello(@AuthenticationPrincipal User user) {
    if (user == null) {
      return ResponseEntity.status(401).build();
    }

    HelloDto hello = new HelloDto(serverName + " - Logged in as " + user.getUsername());
    return ResponseEntity.ok(hello);
  }
}
