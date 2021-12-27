package com.mklinga.reflekt.repositories;

import com.mklinga.reflekt.model.JournalEntry;
import org.springframework.data.repository.CrudRepository;

public interface JournalEntryRepository extends CrudRepository<JournalEntry, Integer> {
}
