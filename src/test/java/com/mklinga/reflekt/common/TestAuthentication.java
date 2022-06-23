package com.mklinga.reflekt.common;

import com.mklinga.reflekt.authentication.model.Role;
import com.mklinga.reflekt.authentication.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TestAuthentication {
  public static final String testUserName = "test-user";
  public static final String testUserPassword = "abc124";

  public static final LocalDateTime testUserCreatedAt = LocalDateTime.now();
  public static final LocalDateTime testUserLastLogin = LocalDateTime.now();

  public static User testUser() {
    User user = new User();
    user.setId(1);
    user.setUsername(testUserName);
    user.setPassword(testUserPassword);
    user.setCreatedAt(testUserCreatedAt);
    user.setLastLogin(testUserLastLogin);
    user.setEntries(new ArrayList<>());

    Role role = new Role();
    role.setRole("USER");

    user.setRoles(Set.of(role));

    return user;
  }

  public static User getUser(String username) {
    if (username.equals(testUserName)) {
      return testUser();
    }

    throw new UsernameNotFoundException("Bad test-user, you should use " + testUserName);
  }
}
