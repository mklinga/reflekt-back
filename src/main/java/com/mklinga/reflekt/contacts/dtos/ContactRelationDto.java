package com.mklinga.reflekt.contacts.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.RelationPredicate;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.Data;

@Data
public class ContactRelationDto {
  private Integer id;
  private UUID subject;
  private RelationPredicate predicate;
  private UUID object;

  @JsonSetter(value = "predicate")
  public void setPredicate(String code) {
    try {
      this.predicate = RelationPredicate.valueOf(code);
    } catch (IllegalArgumentException ex) {
      this.predicate = Stream
          .of(RelationPredicate.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
  }

  @JsonGetter(value = "predicate")
  public String getJsonPredicate() {
    if (this.predicate == null) {
      return null;
    }

    return this.predicate.getCode();
  }
}
