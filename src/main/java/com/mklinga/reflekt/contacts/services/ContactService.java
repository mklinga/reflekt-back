package com.mklinga.reflekt.contacts.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.model.Contact;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import com.mklinga.reflekt.exceptions.ContactExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {
  private final UUID draftId = UUID.fromString("00000000-0000-0000-0000-000000000000");

  private final ContactRepository contactRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ContactService(ContactRepository contactRepository, ModelMapper modelMapper) {
    this.contactRepository = contactRepository;
    this.modelMapper = modelMapper;
  }

  @Transactional(readOnly = true)
  public List<ContactDto> getAllContacts(User user) {
    List<Contact> contacts = this.contactRepository.findAllByOwner(user);
    return contacts.stream()
        .map(contact -> modelMapper.map(contact, ContactDto.class))
        .collect(Collectors.toList());
  }

  private List<ContactRelation> mapRelationDtosToRelations(
      List<ContactRelationDto> relations, List<Contact> contacts) {
    return relations.stream()
        .map(relationDto -> {
          ContactRelation contactRelation = new ContactRelation();
          contactRelation.setId(relationDto.getId());
          contactRelation.setPredicate(relationDto.getPredicate());

          Contact subject = contacts.stream()
              .filter(x -> x.getId().equals(relationDto.getSubject())).findFirst().orElse(null);
          contactRelation.setSubject(subject);

          Contact object = contacts.stream()
              .filter(x -> x.getId().equals(relationDto.getObject())).findFirst().orElse(null);
          contactRelation.setObject(object);

          return contactRelation;
        }).collect(Collectors.toList());
  }

  @Transactional
  public ContactDto addContact(User user, ContactDto newContactDto) throws ContactExistsException {
    if (!newContactDto.getId().equals(draftId)) {
      throw new ContactExistsException("Cannot add a contact with existing ID");
    }

    List<ContactRelationDto> oldRelations = newContactDto.getRelations();

    /* First, we save the contact without relations to obtain an ID for it */
    Contact newContact = modelMapper.map(newContactDto, Contact.class);
    newContact.setOwner(user);
    newContact.setId(null);
    newContact.setRelations(new ArrayList<>());
    Contact savedContact = contactRepository.save(newContact);

    /* Next, we replace all the draft ids in the relations with the one we got back from db */
    oldRelations = oldRelations.stream().map(contactRelationDto -> {
      if (contactRelationDto.getSubject().equals(draftId)) {
        contactRelationDto.setSubject(savedContact.getId());
      }

      if (contactRelationDto.getObject().equals(draftId)) {
        contactRelationDto.setObject(savedContact.getId());
      }
      return contactRelationDto;
    }).collect(Collectors.toList());

    List<Contact> allContacts = contactRepository.findAllByOwner(user);
    savedContact.setRelations(
        mapRelationDtosToRelations(newContactDto.getRelations(), allContacts));

    Contact savedContactWithRelations = contactRepository.save(savedContact);
    return modelMapper.map(savedContactWithRelations, ContactDto.class);
  }
}
