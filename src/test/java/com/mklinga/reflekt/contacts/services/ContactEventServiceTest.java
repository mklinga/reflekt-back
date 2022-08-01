package com.mklinga.reflekt.contacts.services;

import static com.mklinga.reflekt.common.TestAuthentication.testUser;
import static com.mklinga.reflekt.common.configuration.ModelMapperConfiguration.getModelMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactEventDto;
import com.mklinga.reflekt.contacts.model.ContactEvent;
import com.mklinga.reflekt.contacts.model.ContactEventType;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.repositories.ContactEventRepository;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import com.mklinga.reflekt.contacts.utils.DraftItem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class ContactEventServiceTest {

  @Mock
  private ContactEventRepository contactEventRepository;

  @Mock
  private ContactRepository contactRepository;

  private ContactEventService contactEventService;

  private ModelMapper modelMapper;

  @BeforeEach
  public void setUp() {
    this.modelMapper = getModelMapper();
    this.contactEventService = new ContactEventService(
        contactEventRepository, contactRepository, modelMapper);
  }

  @Nested
  @DisplayName("Adding new ContactEvent")
  public class AddNewContactEventTests {

    private final User user = testUser();
    private final UUID testContact1 = UUID.randomUUID();
    private final UUID testContact2 = UUID.randomUUID();

    private List<JpaContact> getTestContacts() {
      JpaContact contact1 = new JpaContact(testContact1, new FullName("First", "Contact"), testUser(), new ArrayList<>());
      JpaContact contact2 = new JpaContact(testContact2, new FullName("Another", "Person"), testUser(), new ArrayList<>());
      return List.of(contact1, contact2);
    }

    @Test
    @DisplayName("Given user has two contacts, he can create new event with them as participants")
    public void addNewContactEventWithTwoParticipants() {
      UUID savedId = UUID.randomUUID();

      when(contactEventRepository.save(any(ContactEvent.class)))
          .thenAnswer(invocation -> {
            ContactEvent argument = invocation.getArgument(0);
            argument.setId(savedId);
            return argument;
          });

      when(contactRepository.findByIdsAndOwner(any(List.class), eq(user.getId())))
          .thenReturn(getTestContacts());

      LocalDate date = LocalDate.now();
      List<JpaContact> testContacts = getTestContacts();
      ContactEventDto newContactEventDto = new ContactEventDto();
      newContactEventDto.setId(DraftItem.id);
      newContactEventDto.setEventDate(date);
      newContactEventDto.setEventType(ContactEventType.IN_PERSON);
      newContactEventDto.setTitle("Test Event");
      newContactEventDto.setDescription("This is the test-event");

      Set<ContactDto> participants = new HashSet<>();
      for (JpaContact jpaContact : testContacts) {
        participants.add(modelMapper.map(jpaContact, ContactDto.class));
      }

      newContactEventDto.setParticipants(participants);

      Optional<ContactEventDto> resultWrapper = contactEventService.addNewContactEvent(user, newContactEventDto);
      assertNotEquals(true, resultWrapper.isEmpty());
      ContactEventDto result = resultWrapper.get();
      assertEquals(savedId, result.getId());

      assertEquals(newContactEventDto.getEventDate(), result.getEventDate());
      assertEquals(newContactEventDto.getEventType(), result.getEventType());
      assertEquals(newContactEventDto.getTitle(), result.getTitle());
      assertEquals(newContactEventDto.getDescription(), result.getDescription());

      Set<ContactDto> resultParticipants = result.getParticipants();
      assertEquals(2, resultParticipants.size());
      assertEquals(true, resultParticipants.containsAll(participants));
    }
  }
}
