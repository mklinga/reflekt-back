package com.mklinga.reflekt.contacts.business;

import static com.mklinga.reflekt.common.TestAuthentication.testUser;
import static org.junit.jupiter.api.Assertions.*;

import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.exceptions.ContactRelationsExistException;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JobInformation;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.model.RelationPredicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ContactTest {

  private final UUID testContactId1 = UUID.randomUUID();
  private final UUID testContactId2 = UUID.randomUUID();

  private List<JpaContact> createTestContacts() {
    return List.of(
        new JpaContact(
            testContactId1,
            new FullName("First", "Contact"),
            testUser(),
            new ArrayList<>(),
            new JobInformation("Title1", "Work1"),
            "Desc1"
        ),
        new JpaContact(
            testContactId2,
            new FullName("The", "Second"),
            testUser(),
            new ArrayList<>(),
            new JobInformation("Title2", "Work2"),
            "Desc2"
        )
    );
  }

  @Test
  void toDtoConvertsContactToContactDto() {
    JpaContact jpaContact = createTestContacts().get(0);
    ContactDto contactDto = jpaContact.toDto();

    assertEquals(testContactId1, contactDto.getId());
    assertEquals("First", contactDto.getFirstName());
    assertEquals("Contact", contactDto.getLastName());
    assertEquals("Title1", contactDto.getJobTitle());
    assertEquals("Work1", contactDto.getWorkplace());
    assertEquals("Desc1", contactDto.getDescription());
    assertEquals(0, contactDto.getRelations().size());
  }

  @Test
  void toDtoConvertsListOfContactsToListOfContactDtos() {
    List<JpaContact> contacts = createTestContacts();
    List<ContactDto> contactDtos = Contact.toDto(contacts);

    ContactDto first = contactDtos.get(0);
    assertEquals(testContactId1, first.getId());
    assertEquals("First", first.getFirstName());
    assertEquals("Contact", first.getLastName());
    assertEquals("Title1", first.getJobTitle());
    assertEquals("Work1", first.getWorkplace());
    assertEquals("Desc1", first.getDescription());
    assertEquals(0, first.getRelations().size());

    ContactDto second = contactDtos.get(1);
    assertEquals(testContactId2, second.getId());
    assertEquals("The", second.getFirstName());
    assertEquals("Second", second.getLastName());
    assertEquals("Title2", second.getJobTitle());
    assertEquals("Work2", second.getWorkplace());
    assertEquals("Desc2", second.getDescription());
    assertEquals(0, second.getRelations().size());
  }

  @Test
  void addRelationAddsNewContactRelationToContactWhenRelationsEmpty() {
    List<JpaContact> contacts = createTestContacts();
    ContactRelation contactRelation = new ContactRelation(
        1, contacts.get(0), RelationPredicate.IS_CHILD_OF, contacts.get(1));

    JpaContact testContact = contacts.get(0);
    testContact.addRelation(contactRelation);

    assertEquals(List.of(contactRelation), testContact.getRelations());
  }

  @Test
  void addRelationAddsNewContactRelationToContactWhenRelationsNotEmpty() {
    List<JpaContact> contacts = createTestContacts();
    ContactRelation existingContactRelation = new ContactRelation(
        1, contacts.get(0), RelationPredicate.IS_CHILD_OF, contacts.get(1));
    ContactRelation contactRelation = new ContactRelation(
        1, contacts.get(0), RelationPredicate.IS_FATHER_OF, contacts.get(1));

    JpaContact testContact = new JpaContact(
        testContactId1,
        new FullName("First", "Contact"),
        testUser(),
        List.of(existingContactRelation),
        new JobInformation("Title1", "Work1"),
        "Desc1"
    );
    testContact.addRelation(contactRelation);

    assertEquals(List.of(existingContactRelation, contactRelation), testContact.getRelations());
  }

  @Test
  void insertInitialRelationsSetsRelationsWhenTheyDontExist() {
    List<JpaContact> contacts = createTestContacts();

    ContactRelation newContactRelation = new ContactRelation(
        1, contacts.get(0), RelationPredicate.IS_FATHER_OF, contacts.get(1));

    JpaContact testContact = new JpaContact(
        testContactId1,
        new FullName("First", "Contact"),
        testUser(),
        new ArrayList<>(),
        new JobInformation("Title1", "Work1"),
        "Desc1"
    );

    testContact.insertInitialRelations(List.of(newContactRelation));
    assertEquals(List.of(newContactRelation), testContact.getRelations());
  }

  @Test
  void insertInitialRelationsFailsIfRelationsExist() {
    List<JpaContact> contacts = createTestContacts();
    ContactRelation existingContactRelation = new ContactRelation(
        1, contacts.get(0), RelationPredicate.IS_CHILD_OF, contacts.get(1));

    ContactRelation newContactRelation = new ContactRelation(
        1, contacts.get(0), RelationPredicate.IS_FATHER_OF, contacts.get(1));

    assertThrows(ContactRelationsExistException.class, () -> {
      JpaContact testContact = new JpaContact(
          testContactId1,
          new FullName("First", "Contact"),
          testUser(),
          List.of(existingContactRelation),
          new JobInformation("Title1", "Work1"),
          "Desc1");

      testContact.insertInitialRelations(List.of(newContactRelation));
    });
  }
}