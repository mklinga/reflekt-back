package com.mklinga.reflekt.contacts.services;

import static com.mklinga.reflekt.common.TestAuthentication.testUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.repositories.ContactRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

  private final UUID testId1 = UUID.randomUUID();
  private final UUID testId2 = UUID.randomUUID();

  @Autowired
  private ModelMapper modelMapper;

  @Mock
  private ContactRepository contactRepository;

  private ContactService contactService;

  @BeforeEach
  public void setup() {
    this.contactService = new ContactService(contactRepository, modelMapper);
  }

  private List<JpaContact> getTestContacts() {
    JpaContact contact1 = new JpaContact(testId1, new FullName("Mr.", "1"), testUser(), new ArrayList<>());
    JpaContact contact2 = new JpaContact(testId2, new FullName("Dr.", "2"), testUser(), new ArrayList<>());
    return List.of(contact1, contact2);
  }

  @Test
  void getAllContacts() {
    List<JpaContact> testContacts = getTestContacts();
    when(contactRepository.findAllByOwner(any(User.class))).thenReturn(testContacts);
    List<ContactDto> result = contactService.getAllContacts(testUser());
    assertEquals(2, result.size());
  }

  @Test
  void addContact() {
  }
}