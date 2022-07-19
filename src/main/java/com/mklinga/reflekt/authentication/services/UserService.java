package com.mklinga.reflekt.authentication.services;

import com.mklinga.reflekt.authentication.dtos.CreateUserDto;
import com.mklinga.reflekt.authentication.model.Role;
import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.authentication.repositories.RoleRepository;
import com.mklinga.reflekt.authentication.repositories.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Provides service methods to interact with the application Users.
 */
@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Main constructor of the UserService.
   *
   * @param userRepository Autowired UserRepository
   * @param roleRepository Autowired RoleRepository
   * @param passwordEncoder Autowired PasswordEncoder used in the application
   *                        (configured in SecurityConfiguration.java)
   */
  @Autowired
  public UserService(UserRepository userRepository, RoleRepository roleRepository,
                     PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Provides a way of adding users to the application.
   * By default, all new users get the role USER.
   *
   * @param createUserDto Data transfer object containing the username and password
   */
  public void addUser(CreateUserDto createUserDto) {
    User newUser = new User();
    newUser.setUsername(createUserDto.getUsername());
    newUser.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
    newUser.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));

    Role userRole = roleRepository.findByRole("USER");
    newUser.setRoles(Set.of(userRole));

    /* TODO: We need also to create a new Contact for the user */

    userRepository.save(newUser);
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
