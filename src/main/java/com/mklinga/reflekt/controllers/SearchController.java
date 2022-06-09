package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.SearchResultDto;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.services.JournalEntryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SearchController takes care of the search of the journal entries.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

  private JournalEntryService journalEntryService;

  @Autowired
  public SearchController(JournalEntryService journalEntryService) {
    this.journalEntryService = journalEntryService;
  }

  /**
   * Search for specific text (query) or a tag name from the journal entries.
   *
   * @param userPrincipal Authenticated user
   * @param query Search query (can be null)
   * @param tag Tag name (can be null)
   * @return List of found SearchResults
   */
  @GetMapping("")
  public ResponseEntity<List<SearchResultDto>> search(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestParam(required = false) String query,
      @RequestParam(required = false) String tag
  ) {

    return ResponseEntity.of(
        journalEntryService.getSearchResults(userPrincipal.getUser(), query, tag)
    );
  }
}
