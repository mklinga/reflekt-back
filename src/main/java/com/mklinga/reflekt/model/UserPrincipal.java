package com.mklinga.reflekt.model;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {
  @Getter
  private com.mklinga.reflekt.model.User user;

  public UserPrincipal(com.mklinga.reflekt.model.User user) {
    super(user.getUsername(), user.getPassword(), user.getRoles());

    this.user = user;
  }
}
