package com.mklinga.reflekt.journal.repositories;

import com.mklinga.reflekt.journal.model.JournalEntry;
import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.model.Image;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides interface for interacting with the Image database table.
 */
public interface ImageRepository extends CrudRepository<Image, UUID> {
  List<Image> findByJournalEntryAndOwnerAndDeleted(
      JournalEntry journalEntry, User user, boolean deleted
  );

  Optional<Image> findByOwnerAndId(User user, UUID id);
}
