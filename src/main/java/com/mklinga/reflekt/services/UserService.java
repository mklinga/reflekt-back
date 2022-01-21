package com.mklinga.reflekt.services;

import com.mklinga.reflekt.dtos.UserDto;
import com.mklinga.reflekt.model.Role;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.repositories.RoleRepository;
import com.mklinga.reflekt.repositories.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private UserRepository userRepository;

  private RoleRepository roleRepository;

  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User addUser(UserDto userDto) {
    User newUser = new User();
    newUser.setUsername(userDto.getUsername());
    newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    newUser.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));

    Role userRole = roleRepository.findByRole("USER");
    newUser.setRoles(Set.of(userRole));

    return userRepository.save(newUser);
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
