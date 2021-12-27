package com.mklinga.reflekt.services;

import com.mklinga.reflekt.dtos.requests.JournalEntryRequestDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.repositories.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public JournalEntry addJournalEntry(JournalEntryRequestDto journalEntryDto) {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        journalEntry.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        journalEntry.setEntry(journalEntryDto.getEntry());

        return journalEntryRepository.save(journalEntry);
    }
}
