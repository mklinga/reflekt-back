package com.mklinga.reflekt.repositories;

import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface JournalEntryRepository extends CrudRepository<JournalEntry, UUID> {
  public Iterable<JournalEntry> findAllByOwner(User user);
  public Optional<JournalEntry> findByOwnerAndId(User user, UUID id);
}
