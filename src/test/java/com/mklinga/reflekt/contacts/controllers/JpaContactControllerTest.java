package com.mklinga.reflekt.contacts.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.common.ApplicationTestConfiguration;
import com.mklinga.reflekt.common.TestAuthentication;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.model.RelationPredicate;
import com.mklinga.reflekt.contacts.services.ContactService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ContactController.class)
@Import(ApplicationTestConfiguration.class)
class JpaContactControllerTest {

  @MockBean
  private ContactService contactService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ModelMapper modelMapper;

  private List<JpaContact> getTestContacts() {

    JpaContact first = new JpaContact(UUID.fromString("e278a279-d5d0-4497-8879-b6dbf6a68431"),
        new FullName("First", "Contact1"), null, new ArrayList<>());

    JpaContact second = new JpaContact(
        UUID.fromString("5adb4e88-26b3-48d2-9505-dad505a8fffe"),
        new FullName("Second", "Contact2"),
        null,
        new ArrayList<>());

    ContactRelation relation1 = new ContactRelation(
        1, first, RelationPredicate.IS_FATHER_OF, second);
    first.addRelation(relation1);

    ContactRelation relation2 = new ContactRelation(
        2, second, RelationPredicate.IS_CHILD_OF, first);
    second.addRelation(relation2);

    return List.of(first, second);
  }

  private List<ContactDto> getTestContactDtos() {
    return getTestContacts()
        .stream()
        .map(contact -> modelMapper.map(contact, ContactDto.class))
        .collect(Collectors.toList());
  }

  @Test
  @WithUserDetails(TestAuthentication.testUserName)
  void getContactsReturnsAListOfContacts() throws Exception {
    List<ContactDto> contactList = getTestContactDtos();
    when(contactService.getAllContacts(Mockito.any(User.class)))
        .thenReturn(contactList);

    String expectedBody = """
      [
        {
          "id":"e278a279-d5d0-4497-8879-b6dbf6a68431",
          "firstName":"First",
          "lastName":"Contact1",
          "relations":[
            {
              "id":1,
              "subject":"e278a279-d5d0-4497-8879-b6dbf6a68431",
              "object":"5adb4e88-26b3-48d2-9505-dad505a8fffe",
              "predicate":"isFatherOf"
            }
          ]
        },
        {
          "id":"5adb4e88-26b3-48d2-9505-dad505a8fffe",
          "firstName":"Second",
          "lastName":"Contact2",
          "relations":[
            {
              "id":2,
              "subject":"5adb4e88-26b3-48d2-9505-dad505a8fffe",
              "object":"e278a279-d5d0-4497-8879-b6dbf6a68431",
              "predicate":"isChildOf"
            }
          ]
        }
      ]
        """;

    mockMvc.perform(MockMvcRequestBuilders.get("/contacts"))
        .andExpectAll(status().isOk(), content().json(expectedBody));
  }

  @Nested
  @DisplayName("addContact")
  public class AddContactTest {

    final ContactDto savedContact = getSavedContact();

    final String postBody = """
          {
            "id":"00000000-0000-0000-0000-000000000000",
            "firstName":"New",
            "lastName":"Contact",
            "relations":
            [
              {
                "subject":"00000000-0000-0000-0000-000000000000",
                "object":"e278a279-d5d0-4497-8879-b6dbf6a68431",
                "predicate":"isFriendOf"
              }
            ]
          }
          """;

    @BeforeEach
    public void setUp() {
      when(contactService.addContact(Mockito.any(User.class), Mockito.any(ContactDto.class)))
          .thenReturn(savedContact);
    }

    private ContactDto getSavedContact() {
      List<JpaContact> existingJpaContacts = getTestContacts();

      JpaContact savedJpaContact = new JpaContact(
          UUID.fromString("cd1ac07f-4959-441d-9c26-8f7b8533e073"),
          new FullName("Saved", "Contact"),
          null,
          new ArrayList<>()
      );

      ContactRelation relation = new ContactRelation(
          3, savedJpaContact, RelationPredicate.IS_FRIEND_OF, existingJpaContacts.get(0));

      savedJpaContact.addRelation(relation);

      return modelMapper.map(savedJpaContact, ContactDto.class);
    }

    @Test
    @WithUserDetails(TestAuthentication.testUserName)
    void parsesInputCorrectly() throws Exception {
      mockMvc
          .perform(post("/contacts").content(postBody).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());

      ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
      ArgumentCaptor<ContactDto> contactArgument = ArgumentCaptor.forClass(ContactDto.class);
      verify(contactService).addContact(userArgument.capture(), contactArgument.capture());

      assertEquals(TestAuthentication.testUser(), userArgument.getValue());

      ContactDto requestContact = contactArgument.getValue();
      assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), requestContact.getId());
      assertEquals("New", requestContact.getFirstName());
      assertEquals("Contact", requestContact.getLastName());
      List<ContactRelationDto> relationDtoList = requestContact.getRelations();
      assertEquals(1, relationDtoList.size());
      assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"),
          relationDtoList.get(0).getSubject());
      assertEquals(UUID.fromString("e278a279-d5d0-4497-8879-b6dbf6a68431"),
          relationDtoList.get(0).getObject());
      assertEquals(RelationPredicate.IS_FRIEND_OF, relationDtoList.get(0).getPredicate());
    }

    @Test
    @WithUserDetails(TestAuthentication.testUserName)
    void sendsOutputCorrectly() throws Exception {
      String expectedResponse = """
          {
            "id":"cd1ac07f-4959-441d-9c26-8f7b8533e073",
            "firstName":"Saved",
            "lastName":"Contact",
            "relations":[
              {
                "id":3,
                "subject":"cd1ac07f-4959-441d-9c26-8f7b8533e073",
                "object":"e278a279-d5d0-4497-8879-b6dbf6a68431",
                "predicate":"isFriendOf"
              }
            ]
          }
            """;

      mockMvc
          .perform(post("/contacts").content(postBody).contentType(MediaType.APPLICATION_JSON))
          .andExpectAll(status().isOk(), content().json(expectedResponse));
    }
  }
}