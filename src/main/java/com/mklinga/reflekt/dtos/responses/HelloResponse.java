package com.mklinga.reflekt.dtos.responses;

import lombok.Data;
import lombok.NonNull;

@Data
public class HelloResponse {
    @NonNull
    private String hello;
}
