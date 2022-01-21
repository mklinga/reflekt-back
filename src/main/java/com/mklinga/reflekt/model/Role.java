package com.mklinga.reflekt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Setter
@Getter
public class Role implements GrantedAuthority {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "role", nullable = false, columnDefinition = "VARCHAR")
  private String role;

  @Override
  public String getAuthority() {
    return this.role;
  }
}
