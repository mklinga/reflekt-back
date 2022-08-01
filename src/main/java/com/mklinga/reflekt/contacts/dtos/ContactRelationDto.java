package com.mklinga.reflekt.contacts.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.model.RelationPredicate;
import com.mklinga.reflekt.contacts.utils.ContactIdResolver;
import com.mklinga.reflekt.contacts.utils.DraftItem;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
    return this.predicate.getCode();
  }

  public ContactRelationDto replaceDraftIds(UUID id) {
    if (subject.equals(DraftItem.id)) {
      subject = id;
    }

    if (object.equals(DraftItem.id)) {
      object = id;
    }

    return this;
  }

  public static List<ContactRelationDto> replaceDraftIds(
      List<ContactRelationDto> contactRelations, UUID newId) {
    return contactRelations.stream()
        .map(contactRelationDto -> contactRelationDto.replaceDraftIds(newId))
        .collect(Collectors.toList());
  }

  public ContactRelation resolve(ContactIdResolver contactIdResolver) {
    return new ContactRelation(
        this.id,
        (JpaContact) contactIdResolver.resolve(this.subject),
        this.predicate,
        (JpaContact) contactIdResolver.resolve(this.object)
    );
  }

  public static List<ContactRelation> resolveList(List<ContactRelationDto> relationDtoList,
                                                  ContactIdResolver contactIdResolver) {
    return relationDtoList.stream()
        .map(contactRelationDto -> contactRelationDto.resolve(contactIdResolver))
        .collect(Collectors.toList());
  }
}
