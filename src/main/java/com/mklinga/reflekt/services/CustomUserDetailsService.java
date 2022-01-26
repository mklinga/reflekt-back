package com.mklinga.reflekt.services;

import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.UserPrincipal;
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

  private PasswordEncoder passwordEncoder;
  private UserService userService;

  @Autowired
  CustomUserDetailsService(PasswordEncoder passwordEncoder, UserService userService) {
    this.passwordEncoder = passwordEncoder;
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
