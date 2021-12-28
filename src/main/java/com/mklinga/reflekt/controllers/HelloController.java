package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.HelloDto;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello Controller, which politely greets the clients.
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
  private String serverName = "Friendly Java server";

  @GetMapping
  public HelloDto sayHello(@RequestParam Optional<String> name) {
    return new HelloDto(name.orElse(serverName));
  }
}
