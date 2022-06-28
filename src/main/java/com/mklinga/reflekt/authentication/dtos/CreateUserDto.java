package com.mklinga.reflekt.authentication.dtos;

import lombok.Data;

/**
 * This is the data transfer object for creating new users. It should not be used for any user-
 * information retrieval by the client (use UserDataDto instead).
 */
@Data
public class CreateUserDto {
  private String username;

  private String password;
}
