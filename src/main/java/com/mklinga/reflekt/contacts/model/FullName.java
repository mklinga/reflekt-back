package com.mklinga.reflekt.contacts.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FullName {
  private String firstName;
  private String lastName;
}
