package com.mklinga.reflekt.contacts.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.business.Contact;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.exceptions.ContactExistsException;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import com.mklinga.reflekt.contacts.utils.DraftItem;
import com.mklinga.reflekt.contacts.utils.InMemoryContactIdResolver;
import java.util.List;
import java.util.Optional;
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

  @Transactional(readOnly = true)
  public Optional<ContactDto> getContactById(UUID id, User user) {
    return Optional
        .ofNullable(this.contactRepository.findByIdAndOwner(id, user))
        .map(contact -> contact.toDto());
  }

  @Transactional
  public ContactDto addContact(User user, ContactDto newContactDto) throws ContactExistsException {
    if (!newContactDto.getId().equals(DraftItem.id)) {
      throw new ContactExistsException("Cannot add a contact with existing ID");
    }

    JpaContact draftContact = JpaContact.createDraftContact(newContactDto, user);

    List<ContactRelationDto> relations = ContactRelationDto
        .replaceDraftIds(newContactDto.getRelations(), draftContact.getId());

    InMemoryContactIdResolver inMemoryContactIdResolver =
        new InMemoryContactIdResolver(contactRepository.findAllByOwner(user));
    inMemoryContactIdResolver.addContact(draftContact);

    List<ContactRelation> jpaContactRelations =
        ContactRelationDto.resolveList(relations, inMemoryContactIdResolver);

    draftContact.insertInitialRelations(jpaContactRelations);

    JpaContact savedJpaContactWithRelations = contactRepository.save(draftContact);
    return modelMapper.map(savedJpaContactWithRelations, ContactDto.class);
  }
}
