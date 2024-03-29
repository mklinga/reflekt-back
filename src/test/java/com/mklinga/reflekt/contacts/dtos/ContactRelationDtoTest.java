package com.mklinga.reflekt.contacts.dtos;

import static com.mklinga.reflekt.common.TestAuthentication.testUser;
import static org.junit.jupiter.api.Assertions.*;

import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JobInformation;
import com.mklinga.reflekt.contacts.model.JpaContact;
import com.mklinga.reflekt.contacts.model.RelationPredicate;
import com.mklinga.reflekt.contacts.utils.ContactIdResolver;
import com.mklinga.reflekt.contacts.utils.DraftItem;
import com.mklinga.reflekt.contacts.utils.InMemoryContactIdResolver;
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
    contactRelationDto.setSubject(DraftItem.id);

    assertEquals(DraftItem.id, contactRelationDto.getSubject());
    contactRelationDto.replaceDraftIds(testSubject);
    assertEquals(testSubject, contactRelationDto.getSubject());
  }

  @Test
  void replaceDraftIdsReplacesObjectDraftIdCorrectly() {
    ContactRelationDto contactRelationDto = createTestDto();
    contactRelationDto.setObject(DraftItem.id);

    assertEquals(DraftItem.id, contactRelationDto.getObject());
    contactRelationDto.replaceDraftIds(testObject);
    assertEquals(testObject, contactRelationDto.getObject());
  }

  @Test
  void testReplaceDraftIdsReplacesAllDraftIdsCorrectly() {
    UUID newId = UUID.randomUUID();
    ContactRelationDto contactRelationDto1 = createTestDto();
    contactRelationDto1.setSubject(DraftItem.id);

    ContactRelationDto contactRelationDto2 = createTestDto();
    contactRelationDto2.setObject(DraftItem.id);

    ContactRelationDto contactRelationDto3 = createTestDto();
    contactRelationDto3.setSubject(DraftItem.id);
    contactRelationDto3.setObject(DraftItem.id);

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
        testSubject,
        new FullName("T", "Subject"),
        testUser(),
        new ArrayList<>(),
        new JobInformation("Title1", "Workplace1"),
        "Description 1"
    );
    JpaContact jpaObject = new JpaContact(
        testObject,
        new FullName("Mr", "Object"),
        testUser(),
        new ArrayList<>(),
        new JobInformation("Title2", "Workplace2"),
        "Description2"
    );

    ContactIdResolver contactIdResolver =
        new InMemoryContactIdResolver(List.of(jpaSubject, jpaObject));

    ContactRelationDto contactRelationDto = createTestDto();
    List<ContactRelation> result = ContactRelationDto.resolveList(
        List.of(contactRelationDto), contactIdResolver);

    assertEquals(1, result.size());
    assertEquals(jpaSubject, result.get(0).getSubject());
    assertEquals(jpaObject, result.get(0).getObject());
    assertEquals(RelationPredicate.IS_FRIEND_OF, result.get(0).getPredicate());
  }
}