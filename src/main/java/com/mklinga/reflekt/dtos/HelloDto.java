package com.mklinga.reflekt.dtos;

import lombok.Data;
import lombok.NonNull;

@Data
public class HelloDto {
    @NonNull
    private String hello;
}
