package com.mklinga.reflekt.repositories;

import com.mklinga.reflekt.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
  public Role findByRole(String role);
}
