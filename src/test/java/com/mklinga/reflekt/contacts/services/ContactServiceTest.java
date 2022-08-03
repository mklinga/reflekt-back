package com.mklinga.reflekt.contacts.services;

import static com.mklinga.reflekt.common.TestAuthentication.testUser;
import static com.mklinga.reflekt.common.configuration.ModelMapperConfiguration.getModelMapper;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.exceptions.ContactExistsException;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JobInformation;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.model.RelationPredicate;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import com.mklinga.reflekt.contacts.utils.DraftItem;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

  private final UUID testId1 = UUID.randomUUID();
  private final UUID testId2 = UUID.randomUUID();

  @Mock
  private ContactRepository contactRepository;

  private ContactService contactService;

  @BeforeEach
  public void setup() {
    ModelMapper modelMapper = getModelMapper();
    this.contactService = new ContactService(contactRepository, modelMapper);
  }

  private List<JpaContact> getTestContacts() {
    JpaContact contact1 = new JpaContact(
        testId1,
        new FullName("Mr.", "1"),
        testUser(),
        new ArrayList<>(),
        new JobInformation("Title 1", "Workplace 1"),
        "Description 1");
    JpaContact contact2 = new JpaContact(
        testId2,
        new FullName("Dr.", "2"),
        testUser(),
        new ArrayList<>(),
        new JobInformation("Title 2", "Workplace 2"),
        "Description 2");
    return List.of(contact1, contact2);
  }

  @Test
  void getAllContactsReturnsAllContactsAsDtos() {
    List<JpaContact> testContacts = getTestContacts();
    when(contactRepository.findAllByOwner(any(User.class))).thenReturn(testContacts);
    List<ContactDto> result = contactService.getAllContacts(testUser());
    assertEquals(2, result.size());

    ContactDto first = result.get(0);
    assertEquals("Mr.", first.getFirstName());
    assertEquals("1", first.getLastName());
    assertEquals("Title 1", first.getJobTitle());
    assertEquals("Workplace 1", first.getWorkplace());
    assertEquals("Description 1", first.getDescription());
    assertEquals(testId1, first.getId());

    ContactDto second = result.get(1);
    assertEquals("Dr.", second.getFirstName());
    assertEquals("2", second.getLastName());
    assertEquals("Title 2", second.getJobTitle());
    assertEquals("Workplace 2", second.getWorkplace());
    assertEquals("Description 2", second.getDescription());
    assertEquals(testId2, second.getId());
  }

  @Nested
  @DisplayName("addContact")
  public class AddContactTests {

    private ContactRelationDto createContactRelationDto() {
      ContactRelationDto contactRelationDto = new ContactRelationDto();
      contactRelationDto.setId(1);
      contactRelationDto.setSubject(testId1);
      contactRelationDto.setObject(testId2);
      contactRelationDto.setPredicate(RelationPredicate.IS_FRIEND_OF.getCode());
      return contactRelationDto;
    }

    @Test
    void itThrowsExceptionIfDtoIdIsNotDraftId() {
      User user = testUser();
      ContactDto newContactDto = new ContactDto();
      newContactDto.setId(UUID.randomUUID());
      newContactDto.setFirstName("New FirstName");
      newContactDto.setLastName("New LastName");
      newContactDto.setRelations(new ArrayList<>());

      assertThrows(ContactExistsException.class, () -> {
        contactService.addContact(user, newContactDto);
      });
    }

    @Test
    void itSavesNewContactAndHandlesDraftIdReplacementsAndReturnsIt() {
      ContactRelationDto contactRelationDto = createContactRelationDto();
      contactRelationDto.setSubject(DraftItem.id);

      ContactDto newContactDto = new ContactDto();
      newContactDto.setId(DraftItem.id);
      newContactDto.setFirstName("New FirstName");
      newContactDto.setLastName("New LastName");
      newContactDto.setJobTitle("Jobtitle");
      newContactDto.setWorkplace("Workplace");
      newContactDto.setDescription("Description");
      newContactDto.setRelations(List.of(contactRelationDto));

      /* Mocks */

      when(contactRepository.save(any(JpaContact.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      List<JpaContact> testContacts = new ArrayList<>(getTestContacts());
      when(contactRepository.findAllByOwner(any(User.class))).thenReturn(testContacts);

      /* Test call */
      ContactDto result = contactService.addContact(testUser(), newContactDto);

      /* Assertion */
      assertNotNull(result);
      assertNotEquals(DraftItem.id, result.getId());
      assertEquals("New FirstName", result.getFirstName());
      assertEquals("New LastName", result.getLastName());
      assertEquals("Jobtitle", result.getJobTitle());
      assertEquals("Workplace", result.getWorkplace());
      assertEquals("Description", result.getDescription());

      List<ContactRelationDto> savedRelations = result.getRelations();
      assertEquals(1, savedRelations.size());
      assertNotEquals(DraftItem.id, savedRelations.get(0).getSubject());
      assertEquals(contactRelationDto.getObject(), savedRelations.get(0).getObject());
      assertEquals(contactRelationDto.getPredicate(), savedRelations.get(0).getPredicate());
    }
  }
}