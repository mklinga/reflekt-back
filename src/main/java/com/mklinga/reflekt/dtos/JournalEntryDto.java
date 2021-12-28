package com.mklinga.reflekt.dtos;

import com.mklinga.reflekt.model.JournalEntry;
import lombok.Data;

import java.util.UUID;

@Data
public class JournalEntryDto {
    private UUID id;

    private String entry;

    public static JournalEntryDto of(JournalEntry journalEntry) {
        JournalEntryDto journalEntryDto = new JournalEntryDto();
        journalEntryDto.setId(journalEntry.getId());
        journalEntryDto.setEntry(journalEntry.getEntry());
        return journalEntryDto;
    }
}
