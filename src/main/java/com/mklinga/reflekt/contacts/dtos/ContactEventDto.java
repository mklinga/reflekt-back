package com.mklinga.reflekt.contacts.dtos;

import com.mklinga.reflekt.contacts.model.ContactEventType;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactEventDto {
  private UUID id;
  private ContactEventType eventType;
  private LocalDate eventDate;
  private String title;
  private String description;
  private Set<ContactDto> participants;
}
