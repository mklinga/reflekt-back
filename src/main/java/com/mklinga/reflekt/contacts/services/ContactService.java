package com.mklinga.reflekt.contacts.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.business.Contact;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import com.mklinga.reflekt.contacts.utils.ContactIdResolver;
import com.mklinga.reflekt.contacts.exceptions.ContactExistsException;
import java.util.List;
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
    return Contact.toDto(jpaContacts);
  }

  @Transactional
  public ContactDto addContact(User user, ContactDto newContactDto) throws ContactExistsException {
    if (!newContactDto.getId().equals(Contact.draftId)) {
      throw new ContactExistsException("Cannot add a contact with existing ID");
    }

    /* First, we save the contact as an Draft to obtain an ID for it */
    JpaContact draftContact = JpaContact.createDraftContact(
        new FullName(newContactDto.getFirstName(), newContactDto.getLastName()), user);
    JpaContact savedDraftContact = contactRepository.save(draftContact);

    /* Next, we replace all the draft ids in the relations with the one we got back from db */
    List<ContactRelationDto> relations = ContactRelationDto
        .replaceDraftIds(newContactDto.getRelations(), savedDraftContact.getId());

    /* Then, map the ContactRelationDto into real ContactRelations */
    ContactIdResolver contactIdResolver =
        new ContactIdResolver(contactRepository.findAllByOwner(user));

    List<ContactRelation> jpaContactRelations =
        ContactRelationDto.resolveList(relations, contactIdResolver);

    /* Insert them into the draft Contact item */
    savedDraftContact.insertInitialRelations(jpaContactRelations);

    JpaContact savedJpaContactWithRelations = contactRepository.save(savedDraftContact);
    return modelMapper.map(savedJpaContactWithRelations, ContactDto.class);
  }
}
