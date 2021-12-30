package com.mklinga.reflekt.converters;

import com.mklinga.reflekt.dtos.JournalEntryDto;
import com.mklinga.reflekt.model.JournalEntry;

public class JournalEntryConverter {
  public static JournalEntryDto toDto(JournalEntry journalEntry) {
    JournalEntryDto journalEntryDto = new JournalEntryDto();
    journalEntryDto.setId(journalEntry.getId());
    journalEntryDto.setEntry(journalEntry.getEntry());
    journalEntryDto.setMood(journalEntry.getMood());
    journalEntryDto.setTitle(journalEntry.getTitle());
    return journalEntryDto;
  }

  public static JournalEntry toEntry(JournalEntryDto journalEntryDto) {
    JournalEntry journalEntry = new JournalEntry();
    journalEntry.setEntry(journalEntryDto.getEntry());
    journalEntry.setMood(journalEntryDto.getMood());
    journalEntry.setTitle(journalEntryDto.getTitle());

    return journalEntry;
  }
}
