package com.mklinga.reflekt.contacts.repositories;

import com.mklinga.reflekt.contacts.model.ContactEvent;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactEventRepository extends JpaRepository<ContactEvent, UUID> {
  @Query(
      nativeQuery = true,
      value = "SELECT DISTINCT(e.*) FROM contact_events e"
          + "  INNER JOIN contact_event_participants p ON e.id = p.contact_event_id"
          + "  INNER JOIN contacts c ON c.id = p.contact_id"
          + "  WHERE c.owner = :owner_id"
          + "  ORDER BY e.event_date DESC")
  List<ContactEvent> findAllByOwner(@Param("owner_id") Integer userId);

  @Query(
      nativeQuery = true,
      value = "SELECT DISTINCT(e.*) FROM contact_events e"
          + "  INNER JOIN contact_event_participants p ON p.contact_event_id = e.id"
          + "  INNER JOIN contacts c ON c.id = p.contact_id"
          + "  WHERE c.owner = :owner_id AND c.id = :contact_id"
          + "  ORDER BY e.event_date DESC")
  List<ContactEvent> findAllByOwnerAndContactId(
      @Param("owner_id") Integer userId, @Param("contact_id") UUID contactId);

  @Query(
      nativeQuery = true,
      value = "   SELECT DISTINCT(ce.*) FROM contact_events ce"
              + " INNER JOIN contact_event_participants cep ON cep.contact_event_id = ce.id"
              + " INNER JOIN contacts c ON c.id = cep.contact_id"
              + " WHERE c.owner = :owner_id"
              + " AND ce.event_date = ("
              + "   SELECT e.entry_date FROM entries e"
              + "     WHERE e.id = :journal_entry_id AND e.owner = :owner_id"
              + " )")
  List<ContactEvent> findAllByJournalEntryId(
      @Param("owner_id") Integer userId, @Param("journal_entry_id") UUID journalEntryId);
}
