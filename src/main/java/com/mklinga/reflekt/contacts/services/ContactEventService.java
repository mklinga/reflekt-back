package com.mklinga.reflekt.contacts.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.dtos.ContactEventDto;
import com.mklinga.reflekt.contacts.repositories.ContactEventRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactEventService {

  private final ContactEventRepository contactEventRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ContactEventService(
      ContactEventRepository contactEventRepository, ModelMapper modelMapper) {
    this.contactEventRepository = contactEventRepository;
    this.modelMapper = modelMapper;
  }

  public List<ContactEventDto> findAll(User user) {
    return contactEventRepository.findAllByOwner(user.getId())
        .stream().map(contactEvent -> modelMapper.map(contactEvent, ContactEventDto.class))
        .collect(Collectors.toList());
  }

}
