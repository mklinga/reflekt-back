package com.mklinga.reflekt.dtos;

import lombok.Data;

/**
 * This is the data transfer object for creating new users. Note that this should never be used for
 * getting the user data, as it contains the password field.
 */
@Data
public class UserDto {
  private String username;

  private String password;
}
