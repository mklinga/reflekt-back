package com.mklinga.reflekt.journal.repositories;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.model.JournalEntry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides the interface methods that are used to interact with JournalEntry repository.
 */
public interface JournalEntryRepository extends CrudRepository<JournalEntry, UUID> {
  List<JournalEntry> findAllByOwner(User user, Sort sort);

  List<JournalEntry> findAllByOwnerAndEntryContainingIgnoreCase(
      User user, String search, Sort sort
  );

  Optional<JournalEntry> findByOwnerAndId(User user, UUID id);
}
