package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.responses.HelloResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/hello")
public class HelloController {
    private String serverName = "Friendly Java server";

    @GetMapping
    public HelloResponse sayHello(@RequestParam Optional<String> name) {
        return new HelloResponse(name.orElse(serverName));
    }

    @PostMapping
    public HelloResponse updateServerName(@RequestParam String name) {
        this.serverName = name;
        return sayHello(Optional.empty());
    }
}
