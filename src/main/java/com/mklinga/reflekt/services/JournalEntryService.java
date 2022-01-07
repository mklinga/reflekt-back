package com.mklinga.reflekt.services;

import com.mklinga.reflekt.dtos.JournalEntryDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.repositories.JournalEntryRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A Service to manipulate journal entries.
 */
@Service
public class JournalEntryService {

  @Autowired
  private JournalEntryRepository journalEntryRepository;

  @Autowired
  private ModelMapper modelMapper;

  public Iterable<JournalEntry> getAllJournalEntries() {
    return journalEntryRepository.findAll();
  }

  public Optional<JournalEntry> getJournalEntry(UUID uuid) {
    return journalEntryRepository.findById(uuid);
  }

  /**
   * Create new Journal entry into the database.

   * @param journalEntryDto new JournalEntry
   * @return saved JournalEntry
   */

  public JournalEntry addJournalEntry(JournalEntryDto journalEntryDto) {
    JournalEntry journalEntry = modelMapper.map(journalEntryDto, JournalEntry.class);
    journalEntry.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
    journalEntry.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

    return journalEntryRepository.save(journalEntry);
  }

  /**
   * Update existing JournalEntry in the database.

   * @param uuid UUID of the existing item
   * @param journalEntryDto Updated item
   * @return Updated item
   */
  public Optional<JournalEntry> updateJournalEntry(UUID uuid, JournalEntryDto journalEntryDto) {
    return journalEntryRepository
        .findById(uuid)
        .map(entry -> {
          entry.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
          entry.setEntry(journalEntryDto.getEntry());
          return journalEntryRepository.save(entry);
        });
  }

  /**
   * Delete existing JournalEntry by UUID.

   * @param uuid UUID to delete
   * @return true when successful, false when item not found
   */
  public boolean deleteJournalEntry(UUID uuid) {
    if (journalEntryRepository.findById(uuid).isEmpty()) {
      return false;
    }

    journalEntryRepository.deleteById(uuid);
    return true;
  }
}
