package com.mklinga.reflekt.contacts.dtos;

import com.mklinga.reflekt.contacts.model.RelationPredicate;
import java.util.UUID;
import lombok.Data;

@Data
public class ContactRelationDto {
  private UUID subject;
  private RelationPredicate predicate;
  private UUID object;
}
