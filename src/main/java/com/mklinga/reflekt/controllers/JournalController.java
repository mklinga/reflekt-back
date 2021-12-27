package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.requests.JournalEntryRequestDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.services.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("")
    public ResponseEntity<String> getJournalEntry() {
        // fetch the journal entry from db or return 404

        JournalEntryRequestDto journalEntryRequestDto = new JournalEntryRequestDto();
        journalEntryRequestDto.setEntry("TEST entry no 1.");
        JournalEntry journalEntry = journalEntryService.addJournalEntry(journalEntryRequestDto);

        return ResponseEntity.ok(journalEntry.getId().toString());
    }
}
