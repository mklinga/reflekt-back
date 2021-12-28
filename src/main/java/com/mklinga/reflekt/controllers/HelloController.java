package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.HelloDto;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/hello")
public class HelloController {
    private String serverName = "Friendly Java server";

    @GetMapping
    public HelloDto sayHello(@RequestParam Optional<String> name) {
        return new HelloDto(name.orElse(serverName));
    }

    @PostMapping
    public HelloDto updateServerName(@RequestParam String name) {
        this.serverName = name;
        return sayHello(Optional.empty());
    }
}
