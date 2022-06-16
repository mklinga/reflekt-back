package com.mklinga.reflekt.authentication.repositories;

import com.mklinga.reflekt.authentication.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides the interface for interacting with the User repository.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
  public User findByUsername(String username);
}
