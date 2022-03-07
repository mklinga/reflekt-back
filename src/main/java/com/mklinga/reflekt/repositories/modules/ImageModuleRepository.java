package com.mklinga.reflekt.repositories.modules;

import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.modules.ImageModule;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides interface for interacting with the ImageModule database table.
 */
public interface ImageModuleRepository extends CrudRepository<ImageModule, UUID> {
  public List<ImageModule> findByJournalEntry(JournalEntry journalEntry);
}
