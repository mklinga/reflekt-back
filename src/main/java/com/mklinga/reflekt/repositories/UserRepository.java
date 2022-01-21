package com.mklinga.reflekt.repositories;

import com.mklinga.reflekt.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
  public User findByUsername(String username);
}
