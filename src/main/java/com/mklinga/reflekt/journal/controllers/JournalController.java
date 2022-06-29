package com.mklinga.reflekt.journal.controllers;

import com.mklinga.reflekt.authentication.model.UserPrincipal;
import com.mklinga.reflekt.common.model.Navigable;
import com.mklinga.reflekt.common.model.NavigationData;
import com.mklinga.reflekt.journal.dtos.JournalEntryDto;
import com.mklinga.reflekt.journal.model.JournalEntry;
import com.mklinga.reflekt.journal.services.JournalEntryService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main controller for journal entries.
 */
@RestController
@RequestMapping("/journal")
public class JournalController {

  private final JournalEntryService journalEntryService;
  private final ModelMapper modelMapper;

  @Autowired
  public JournalController(JournalEntryService journalEntryService, ModelMapper modelMapper) {
    this.journalEntryService = journalEntryService;
    this.modelMapper = modelMapper;
  }

  /**
   * Returns JournalListItem for all the entries.
   *
   * @return list of JournalListItems
   */
  @GetMapping("")
  public ResponseEntity<List<JournalEntryDto>> getJournalEntries(
      @AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) String search) {
    List<JournalEntryDto> all = journalEntryService.getAllEntries(user, search);

    return ResponseEntity.ok(all);
  }

  /**
   * Returns a single JournalEntry by its UUID.
   *
   * @param uuid The id of the JournalEntry
   * @return JournalEntry or 404 if not found
   */
  @GetMapping("/{uuid}")
  public ResponseEntity<Navigable<JournalEntryDto>> getJournalEntry(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable UUID uuid) {
    Optional<JournalEntryDto> data = journalEntryService
        .getJournalEntry(user, uuid)
        .map(entry -> modelMapper.map(entry, JournalEntryDto.class));

    return data.map(journalEntryDto -> {
      NavigationData navigationData = journalEntryService
          .getEntryNavigationData(user, journalEntryDto.getId());

      return ResponseEntity.ok(new Navigable<>(journalEntryDto, navigationData));
    }).orElse(ResponseEntity.notFound().build());
  }

  /**
   * Updates an existing JournalEntry.
   *
   * @param uuid         The id of the JournalEntry
   * @param journalEntry The updated JournalEntry
   * @return The updated JournalEntry or 404 if not found
   */
  @PutMapping("/{uuid}")
  public ResponseEntity<JournalEntryDto> updateJournalEntry(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable UUID uuid,
      @RequestBody JournalEntryDto journalEntry) {
    return ResponseEntity.of(journalEntryService
        .updateJournalEntry(user, uuid, journalEntry)
        .map(entry -> modelMapper.map(entry, JournalEntryDto.class)));
  }

  /**
   * Deletes an existing JournalEntry.
   *
   * @param uuid The id of JournalEntry
   * @return 200 OK or 404 Not found
   */
  @DeleteMapping("{uuid}")
  public ResponseEntity<Void> deleteJournalEntry(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable UUID uuid) {
    HttpStatus status =
        journalEntryService.deleteJournalEntry(user, uuid) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    return ResponseEntity.status(status).build();
  }

  /**
   * Creates a new JournalEntry.
   *
   * @param newJournalEntry New JournalEntry
   * @return Created JournalEntry
   */
  @PostMapping("")
  public ResponseEntity<JournalEntryDto> createJournalEntry(
      @AuthenticationPrincipal UserPrincipal user,
      @RequestBody JournalEntryDto newJournalEntry) {
    JournalEntry journalEntry = journalEntryService.addJournalEntry(user, newJournalEntry);
    return ResponseEntity.ok(modelMapper.map(journalEntry, JournalEntryDto.class));
  }
}
