package com.mklinga.reflekt.repositories;

import com.mklinga.reflekt.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides the interface for interacting with the authentication role - table in the database.
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
  public Role findByRole(String role);
}
