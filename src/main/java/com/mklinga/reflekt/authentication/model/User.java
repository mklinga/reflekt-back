package com.mklinga.reflekt.authentication.model;

import com.mklinga.reflekt.authentication.model.Role;
import com.mklinga.reflekt.journal.model.JournalEntry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity for the application user.
 */
@Entity
@Table(name = "users")
@Setter
@Getter
@EqualsAndHashCode
public class User {
  @Id
  @GeneratedValue(generator = "users_id_gen")
  @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
  private Integer id;

  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
  private LocalDateTime createdAt;

  @Column(name = "last_login", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
  private LocalDateTime lastLogin;

  @Column(name = "username", nullable = false, columnDefinition = "VARCHAR(50)")
  private String username;

  @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(500)")
  private String password;

  @ManyToMany
  @JoinTable(name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  Set<Role> roles;

  @OneToMany(mappedBy = "owner")
  List<JournalEntry> entries;
}
