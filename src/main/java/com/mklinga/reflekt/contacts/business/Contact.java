package com.mklinga.reflekt.contacts.business;

import static com.mklinga.reflekt.common.configuration.ModelMapperConfiguration.getModelMapper;

import com.mklinga.reflekt.contacts.exceptions.ContactRelationsExistException;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public abstract class Contact {
  public static final UUID draftId = UUID.fromString("00000000-0000-0000-0000-000000000000");

  protected final ModelMapper modelMapper = getModelMapper();

  public abstract FullName getFullName();

  protected abstract void setFullName(FullName fullName);

  public abstract UUID getId();

  public abstract List<ContactRelation> getRelations();

  protected abstract void setRelations(List<ContactRelation> relations);

  public ContactDto toDto() {
    return modelMapper.map(this, ContactDto.class);
  }

  public static List<ContactDto> toDto(List<? extends Contact> contactList) {
    return contactList.stream()
        .map(Contact::toDto)
        .collect(Collectors.toList());
  }

  public void addRelation(ContactRelation relation) {
    List<ContactRelation> relations = new ArrayList<>(this.getRelations());
    relations.add(relation);
    this.setRelations(relations);
  }

  public void insertInitialRelations(List<ContactRelation> relations) {
    if (!this.getRelations().isEmpty()) {
      throw new ContactRelationsExistException(
          "Cannot insert initial relations when relations is not empty"
      );
    }

    this.setRelations(relations);
  }
}
