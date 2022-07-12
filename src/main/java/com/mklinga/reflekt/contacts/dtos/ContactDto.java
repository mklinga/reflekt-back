package com.mklinga.reflekt.contacts.dtos;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ContactDto {
  private UUID id;
  private String firstName;
  private String lastName;

  private List<ContactRelationDto> relations;
}
