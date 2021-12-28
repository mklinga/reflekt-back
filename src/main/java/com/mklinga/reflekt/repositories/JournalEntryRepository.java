package com.mklinga.reflekt.repositories;

import com.mklinga.reflekt.model.JournalEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface JournalEntryRepository extends CrudRepository<JournalEntry, UUID> {
}
