package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.UserDto;
import com.mklinga.reflekt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("")
  public ResponseEntity addUser(@RequestBody UserDto user) {
    userService.addUser(user);
    return ResponseEntity.ok().build();
  }

}
