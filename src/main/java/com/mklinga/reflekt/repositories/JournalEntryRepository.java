package com.mklinga.reflekt.repositories;

import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides the interface methods that are used to interact with JournalEntry repository.
 */
public interface JournalEntryRepository extends CrudRepository<JournalEntry, UUID> {
  public Iterable<JournalEntry> findAllByOwner(User user, Sort sort);

  public Optional<JournalEntry> findByOwnerAndId(User user, UUID id);
}
