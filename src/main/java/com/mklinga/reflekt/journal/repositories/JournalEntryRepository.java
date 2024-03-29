package com.mklinga.reflekt.journal.repositories;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.interfaces.RawEntryTagsResult;
import com.mklinga.reflekt.journal.interfaces.RawJournalEntryResult;
import com.mklinga.reflekt.journal.model.JournalEntry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Provides the interface methods that are used to interact with JournalEntry repository.
 */
public interface JournalEntryRepository extends CrudRepository<JournalEntry, UUID> {
  List<JournalEntry> findAllByOwner(User user, Pageable pageable);

  @Query(nativeQuery = true,
      value = "   SELECT cast(e.id as varchar), e.entry_date AS entryDate,"
              + " e.created_at AS createdAt, e.updated_at AS updatedAt, e.mood, e.title, e.entry"
              + " FROM entries e WHERE e.owner = :owner"
              + " ORDER BY entry_date DESC")
  List<RawJournalEntryResult> findAllByOwner(@Param("owner") Integer owner);

  @Query(nativeQuery = true,
      value = "   SELECT cast(te.entry_id as varchar) as entryId,"
              + " cast(te.tag_id as varchar) as tagId, t.name, t.color FROM tag_entries te"
              + " INNER JOIN tags t ON te.tag_id = t.id"
              + " WHERE te.entry_id IN :entries")
  List<RawEntryTagsResult> findTagsForEntries(@Param("entries") List<UUID> entries);

  @Query(nativeQuery = true,
      value = "   SELECT e.* FROM entries e"
              + " WHERE e.owner = :owner AND e.id IN ("
              + "   SELECT te.entry_id FROM tag_entries te"
              + "   INNER JOIN tags t ON t.id = te.tag_id"
              + "   WHERE t.name = :tag_name)"
              + " ORDER BY e.entry_date DESC")
  List<JournalEntry> findEntriesByTagName(@Param("owner") Integer owner,
                                          @Param("tag_name") String tagName);

  List<JournalEntry> findAllByOwnerAndEntryContainingIgnoreCase(
      User user, String search, Sort sort
  );

  Optional<JournalEntry> findByOwnerAndId(User user, UUID id);
}
