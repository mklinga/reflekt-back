package com.mklinga.reflekt.contacts.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.model.Contact;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
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
    List<Contact> contacts = this.contactRepository.findAllByOwner(user);
    return contacts.stream()
        .map(contact -> modelMapper.map(contact, ContactDto.class))
        .collect(Collectors.toList());
  }
}
