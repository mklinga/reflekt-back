package com.mklinga.reflekt.contacts.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.business.Contact;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.exceptions.ContactExistsException;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import com.mklinga.reflekt.contacts.utils.ContactIdResolver;
import java.util.List;
import java.util.UUID;
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

    JpaContact draftContact = JpaContact.createDraftContact(
        new FullName(newContactDto.getFirstName(), newContactDto.getLastName()), user);

    /* Next, we replace all the draft ids in the relations */
    List<ContactRelationDto> relations = ContactRelationDto
        .replaceDraftIds(newContactDto.getRelations(), draftContact.getId());

    /* Then, map the ContactRelationDto into real ContactRelations */
    ContactIdResolver contactIdResolver =
        new ContactIdResolver(contactRepository.findAllByOwner(user));
    contactIdResolver.addContact(draftContact);

    List<ContactRelation> jpaContactRelations =
        ContactRelationDto.resolveList(relations, contactIdResolver);

    /* Insert them into the draft Contact item */
    draftContact.insertInitialRelations(jpaContactRelations);

    JpaContact savedJpaContactWithRelations = contactRepository.save(draftContact);
    return modelMapper.map(savedJpaContactWithRelations, ContactDto.class);
  }
}
