package com.mklinga.reflekt.repositories.modules;

import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.modules.ImageModule;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides interface for interacting with the ImageModule database table.
 */
public interface ImageModuleRepository extends CrudRepository<ImageModule, UUID> {
  List<ImageModule> findByJournalEntryAndOwnerAndDeleted(
      JournalEntry journalEntry, User user, boolean deleted
  );

  Optional<ImageModule> findByOwnerAndId(User user, UUID id);
}
