package com.mklinga.reflekt.contacts.dtos;

import static com.mklinga.reflekt.common.TestAuthentication.testUser;
import static org.junit.jupiter.api.Assertions.*;

import com.mklinga.reflekt.contacts.business.Contact;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.model.RelationPredicate;
import com.mklinga.reflekt.contacts.utils.ContactIdResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ContactRelationDtoTest {

  private final UUID testSubject = UUID.randomUUID();
  private final UUID testObject = UUID.randomUUID();

  private ContactRelationDto createTestDto() {
    ContactRelationDto contactRelationDto = new ContactRelationDto();
    contactRelationDto.setId(1);
    contactRelationDto.setSubject(testSubject);
    contactRelationDto.setObject(testObject);
    contactRelationDto.setPredicate(RelationPredicate.IS_FRIEND_OF.getCode());
    return contactRelationDto;
  }

  @Test
  void replaceDraftIdsReplacesSubjectDraftIdCorrectly() {
    ContactRelationDto contactRelationDto = createTestDto();
    contactRelationDto.setSubject(Contact.draftId);

    assertEquals(Contact.draftId, contactRelationDto.getSubject());
    contactRelationDto.replaceDraftIds(testSubject);
    assertEquals(testSubject, contactRelationDto.getSubject());
  }

  @Test
  void replaceDraftIdsReplacesObjectDraftIdCorrectly() {
    ContactRelationDto contactRelationDto = createTestDto();
    contactRelationDto.setObject(Contact.draftId);

    assertEquals(Contact.draftId, contactRelationDto.getObject());
    contactRelationDto.replaceDraftIds(testObject);
    assertEquals(testObject, contactRelationDto.getObject());
  }

  @Test
  void testReplaceDraftIdsReplacesAllDraftIdsCorrectly() {
    UUID newId = UUID.randomUUID();
    ContactRelationDto contactRelationDto1 = createTestDto();
    contactRelationDto1.setSubject(Contact.draftId);

    ContactRelationDto contactRelationDto2 = createTestDto();
    contactRelationDto2.setObject(Contact.draftId);

    ContactRelationDto contactRelationDto3 = createTestDto();
    contactRelationDto3.setSubject(Contact.draftId);
    contactRelationDto3.setObject(Contact.draftId);

    List<ContactRelationDto> draftRelations = List.of(
        contactRelationDto1, contactRelationDto2, contactRelationDto3);

    List<ContactRelationDto> result = ContactRelationDto.replaceDraftIds(draftRelations, newId);

    assertEquals(3, result.size());
    assertEquals(newId, result.get(0).getSubject());
    assertEquals(testObject, result.get(0).getObject());

    assertEquals(testSubject, result.get(1).getSubject());
    assertEquals(newId, result.get(1).getObject());

    assertEquals(newId, result.get(2).getSubject());
    assertEquals(newId, result.get(2).getObject());
  }

  @Test
  void resolveListResolvesContactsForListOfContactRelationsCorrectlyForOneItem() {
    JpaContact jpaSubject = new JpaContact(
        testSubject, new FullName("T", "Subject"), testUser(), new ArrayList<>());
    JpaContact jpaObject = new JpaContact(
        testObject, new FullName("Mr", "Object"), testUser(), new ArrayList<>());

    ContactIdResolver contactIdResolver = new ContactIdResolver(List.of(jpaSubject, jpaObject));
    ContactRelationDto contactRelationDto = createTestDto();
    List<ContactRelation> result = ContactRelationDto.resolveList(
        List.of(contactRelationDto), contactIdResolver);

    assertEquals(1, result.size());
    assertEquals(jpaSubject, result.get(0).getSubject());
    assertEquals(jpaObject, result.get(0).getObject());
    assertEquals(RelationPredicate.IS_FRIEND_OF, result.get(0).getPredicate());
  }
}