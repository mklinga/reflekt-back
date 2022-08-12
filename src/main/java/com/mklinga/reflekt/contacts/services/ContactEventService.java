package com.mklinga.reflekt.contacts.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.dtos.ContactEventDto;
import com.mklinga.reflekt.contacts.model.ContactEvent;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.repositories.ContactEventRepository;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactEventService {

  private final ContactEventRepository contactEventRepository;
  private final ContactRepository contactRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ContactEventService(ContactEventRepository contactEventRepository,
                             ContactRepository contactRepository,
                             ModelMapper modelMapper) {
    this.contactEventRepository = contactEventRepository;
    this.contactRepository = contactRepository;
    this.modelMapper = modelMapper;
  }

  public List<ContactEventDto> findAll(User user) {
    return contactEventRepository.findAllByOwner(user.getId()).stream()
        .map(contactEvent -> modelMapper.map(contactEvent, ContactEventDto.class))
        .collect(Collectors.toList());
  }

  public List<ContactEventDto> findForContactId(User user, UUID contactId) {
    return contactEventRepository
        .findAllByOwnerAndContactId(user.getId(), contactId).stream()
        .map(event -> modelMapper.map(event, ContactEventDto.class))
        .collect(Collectors.toList());
  }

  public List<ContactEventDto> findForJournalEntry(User user, UUID journalEntryId) {
    return contactEventRepository
        .findAllByJournalEntryId(user.getId(), journalEntryId)
        .stream().map(event -> modelMapper.map(event, ContactEventDto.class))
        .collect(Collectors.toList());
  }

  public Optional<ContactEventDto> addNewContactEvent(User user, ContactEventDto contactEventDto) {
    List<UUID> participantIds = contactEventDto
        .getParticipants().stream().map(x -> x.getId()).collect(Collectors.toList());
    List<JpaContact> userOwnedParticipants = contactRepository
        .findByIdsAndOwner(participantIds, user.getId());

    ContactEvent contactEvent = modelMapper.map(contactEventDto, ContactEvent.class);
    contactEvent.setParticipants(new HashSet<>(userOwnedParticipants));
    contactEvent = contactEventRepository.save(contactEvent);
    return Optional.of(modelMapper.map(contactEvent, ContactEventDto.class));
  }
}
