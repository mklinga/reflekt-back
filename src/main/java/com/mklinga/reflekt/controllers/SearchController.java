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

@RestController
@RequestMapping("/search")
public class SearchController {

  private JournalEntryService journalEntryService;

  @Autowired
  public SearchController(JournalEntryService journalEntryService) {
    this.journalEntryService = journalEntryService;
  }

  @GetMapping("")
  public ResponseEntity<List<SearchResultDto>> search(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestParam(required = false) String query,
      @RequestParam(required = false) String tag
      ) {

    return ResponseEntity.of(journalEntryService.getSearchResults(userPrincipal.getUser(), query, tag));
  }
}
