package com.mklinga.reflekt.converters;

import com.mklinga.reflekt.dtos.JournalEntryDto;
import com.mklinga.reflekt.model.JournalEntry;

/**
 * Converter to convert between JournalEntry <-> JournalEntryDto.
 */
public class JournalEntryConverter {
  /**
   * Convert JournalEntry into JournalEntryDto.
   *
   * @param journalEntry JournalEntry
   * @return JournalEntryDto
   */
  public static JournalEntryDto toDto(JournalEntry journalEntry) {
    JournalEntryDto journalEntryDto = new JournalEntryDto();
    journalEntryDto.setId(journalEntry.getId());
    journalEntryDto.setEntry(journalEntry.getEntry());
    journalEntryDto.setMood(journalEntry.getMood());
    journalEntryDto.setTitle(journalEntry.getTitle());
    journalEntryDto.setCreatedAt(journalEntry.getCreatedAt());
    journalEntryDto.setUpdatedAt(journalEntry.getUpdatedAt());
    return journalEntryDto;
  }

  /**
   * Convert JournalEntryDto into JournalEntry.
   *
   * @param journalEntryDto JournalEntryDto
   * @return JournalEntry
   */
  public static JournalEntry toEntry(JournalEntryDto journalEntryDto) {
    JournalEntry journalEntry = new JournalEntry();
    journalEntry.setEntry(journalEntryDto.getEntry());
    journalEntry.setMood(journalEntryDto.getMood());
    journalEntry.setTitle(journalEntryDto.getTitle());
    journalEntry.setCreatedAt(journalEntryDto.getCreatedAt());
    journalEntry.setUpdatedAt(journalEntryDto.getUpdatedAt());

    return journalEntry;
  }
}
