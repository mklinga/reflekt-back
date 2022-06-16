package com.mklinga.reflekt.authentication.model;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

/**
 * This is the main user principal in the application after the session has been authenticated.
 * Besides taking advantage of the Spring Boot's default User, we also store the User model from
 * the database with this UserPrincipal (that is used to verify e.g. that the current user has
 * a permission for certain database activities, such as only seeing/manipulating his own entries).
 */
public class UserPrincipal extends User {
  @Getter
  private final com.mklinga.reflekt.authentication.model.User user;

  /**
   * The constructor for the UserPrincipal takes the authenticated model from the database and
   * passes the relevant fields on to the Spring Boot's User.
   *
   * @param user Db model of the authenticated user.
   */
  public UserPrincipal(com.mklinga.reflekt.authentication.model.User user) {
    super(user.getUsername(), user.getPassword(), user.getRoles());

    this.user = user;
  }
}
