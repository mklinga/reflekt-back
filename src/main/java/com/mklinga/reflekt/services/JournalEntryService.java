package com.mklinga.reflekt.services;

import com.mklinga.reflekt.dtos.JournalEntryDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.repositories.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public Iterable<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getJournalEntry(UUID uuid) {
        return journalEntryRepository.findById(uuid);
    }

    public JournalEntry addJournalEntry(JournalEntryDto journalEntryDto) {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        journalEntry.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        journalEntry.setEntry(journalEntryDto.getEntry());

        return journalEntryRepository.save(journalEntry);
    }

    public Optional<JournalEntry> updateJournalEntry(UUID uuid, JournalEntryDto journalEntryDto) {
        return journalEntryRepository
                .findById(uuid)
                .map(entry -> {
                    entry.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
                    entry.setEntry(journalEntryDto.getEntry());
                    return journalEntryRepository.save(entry);
                });
    }

    public boolean deleteJournalEntry(UUID uuid) {
        if (journalEntryRepository.findById(uuid).isEmpty()) {
            return false;
        }

        journalEntryRepository.deleteById(uuid);
        return true;
    }
}
