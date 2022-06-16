package com.mklinga.reflekt.authentication.dtos;

import lombok.Data;
import lombok.NonNull;

/**
 * DTO for greeting the client.
 */
@Data
public class HelloDto {
  @NonNull
  private String hello;
}
