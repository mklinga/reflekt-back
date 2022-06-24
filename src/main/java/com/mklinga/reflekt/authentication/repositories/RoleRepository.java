package com.mklinga.reflekt.authentication.repositories;

import com.mklinga.reflekt.authentication.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides the interface for interacting with the authentication role - table in the database.
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
  Role findByRole(String role);
}
