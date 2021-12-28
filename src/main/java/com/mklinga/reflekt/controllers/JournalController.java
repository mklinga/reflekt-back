package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.JournalEntryDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.services.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("")
    public ResponseEntity<List<JournalEntryDto>> getJournalEntries() {
        List<JournalEntryDto> all = new ArrayList<>();
        journalEntryService.getAllJournalEntries().forEach(entry -> {
            all.add(JournalEntryDto.of(entry));
        });

        return ResponseEntity.ok(all);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<JournalEntryDto> getJournalEntry(@PathVariable UUID uuid) {
        return journalEntryService
                .getJournalEntry(uuid)
                .map(JournalEntryDto::of)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<JournalEntryDto> updateJournalEntry(@PathVariable UUID uuid, @RequestBody JournalEntryDto journalEntry) {
        return journalEntryService
                .updateJournalEntry(uuid, journalEntry)
                .map(JournalEntryDto::of)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteJournalEntry(@PathVariable UUID uuid) {
        HttpStatus status = journalEntryService.deleteJournalEntry(uuid) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).build();
    }

    @PostMapping("")
    public ResponseEntity<JournalEntryDto> createJournalEntry(@RequestBody JournalEntryDto newJournalEntry) {
        JournalEntry journalEntry = journalEntryService.addJournalEntry(newJournalEntry);
        return ResponseEntity.ok(JournalEntryDto.of(journalEntry));
    }
}
