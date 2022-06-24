package com.mklinga.reflekt.authentication.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.authentication.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom user details service that is being used in authenticating the user.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Autowired
  CustomUserDetailsService(PasswordEncoder passwordEncoder, UserService userService) {
    this.userService = userService;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User dbUser = userService.findUserByUsername(username);

    if (dbUser == null) {
      throw new UsernameNotFoundException("Username not found");
    }

    return new UserPrincipal(dbUser);
  }
}
