package com.mklinga.reflekt.contacts.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.business.Contact;
import com.mklinga.reflekt.business.FullName;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import com.mklinga.reflekt.exceptions.ContactExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {
  private final ContactRepository contactRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ContactService(ContactRepository contactRepository, ModelMapper modelMapper) {
    this.contactRepository = contactRepository;
    this.modelMapper = modelMapper;
  }

  @Transactional(readOnly = true)
  public List<ContactDto> getAllContacts(User user) {
    List<JpaContact> jpaContacts = this.contactRepository.findAllByOwner(user);
    return jpaContacts.stream()
        .map(Contact::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public ContactDto addContact(User user, ContactDto newContactDto) throws ContactExistsException {
    if (!newContactDto.getId().equals(Contact.draftId)) {
      throw new ContactExistsException("Cannot add a contact with existing ID");
    }

    /* First, we save the contact as an Draft to obtain an ID for it */
    JpaContact newJpaContact = JpaContact.createDraftContact(
        new FullName(newContactDto.getFirstName(), newContactDto.getLastName()), user);
    JpaContact savedJpaContact = contactRepository.save(newJpaContact);

    /* Next, we replace all the draft ids in the relations with the one we got back from db */
    List<JpaContact> allJpaContacts = contactRepository.findAllByOwner(user);
    savedJpaContact.insertDraftableRelations(newContactDto, allJpaContacts);

    JpaContact savedJpaContactWithRelations = contactRepository.save(savedJpaContact);
    return modelMapper.map(savedJpaContactWithRelations, ContactDto.class);
  }
}
