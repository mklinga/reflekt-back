package com.mklinga.reflekt.authentication.services;

import com.mklinga.reflekt.authentication.dtos.UserDto;
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
   * @param userDto Data transfer object containing the username and password
   */
  public void addUser(UserDto userDto) {
    User newUser = new User();
    newUser.setUsername(userDto.getUsername());
    newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    newUser.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));

    Role userRole = roleRepository.findByRole("USER");
    newUser.setRoles(Set.of(userRole));

    userRepository.save(newUser);
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
