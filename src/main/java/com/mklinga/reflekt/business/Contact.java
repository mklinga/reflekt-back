package com.mklinga.reflekt.business;

import static com.mklinga.reflekt.common.configuration.ModelMapperConfiguration.getModelMapper;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.JpaContact;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public abstract class Contact {
  public static final UUID draftId = UUID.fromString("00000000-0000-0000-0000-000000000000");

  protected final ModelMapper modelMapper = getModelMapper();

  public abstract FullName getFullName();

  public abstract UUID getId();

  public abstract List<ContactRelation> getRelations();

  public abstract void setFullName(FullName fullName);

  protected abstract void setOwner(User user);

  protected abstract void setRelations(List<ContactRelation> relations);

  public ContactDto toDto() {
    return modelMapper.map(this, ContactDto.class);
  }

  public void addRelation(ContactRelation relation) {
    List<ContactRelation> relations = this.getRelations();
    relations.add(relation);
  }

  private List<ContactRelation> mapRelationDtosToRelations(
      List<ContactRelationDto> relations, List<JpaContact> jpaContacts) {
    return relations.stream()
        .map(relationDto -> {
          ContactRelation contactRelation = new ContactRelation();
          contactRelation.setId(relationDto.getId());
          contactRelation.setPredicate(relationDto.getPredicate());

          JpaContact subject = jpaContacts.stream()
              .filter(x -> x.getId().equals(relationDto.getSubject())).findFirst().orElse(null);
          contactRelation.setSubject(subject);

          JpaContact object = jpaContacts.stream()
              .filter(x -> x.getId().equals(relationDto.getObject())).findFirst().orElse(null);
          contactRelation.setObject(object);

          return contactRelation;
        }).collect(Collectors.toList());
  }

  public void insertDraftableRelations(
      ContactDto newContactDto, List<JpaContact> allJpaContacts) {
    List<ContactRelationDto> oldRelations = newContactDto
        .getRelations()
        .stream()
        .map(contactRelationDto -> {
          if (contactRelationDto.getSubject().equals(draftId)) {
            contactRelationDto.setSubject(this.getId());
          }

          if (contactRelationDto.getObject().equals(draftId)) {
            contactRelationDto.setObject(this.getId());
          }
          return contactRelationDto;
        }).collect(Collectors.toList());

    this.setRelations(mapRelationDtosToRelations(newContactDto.getRelations(), allJpaContacts));
  }
}
