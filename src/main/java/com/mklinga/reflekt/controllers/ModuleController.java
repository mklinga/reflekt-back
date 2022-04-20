package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.ModuleDataDto;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.model.modules.Tag;
import com.mklinga.reflekt.services.modules.ModuleService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Single controller to work with all the different modules in the application related to the
 * JournalEntries.
 */
@RestController
@RequestMapping("/modules")
public class ModuleController {
  private final ModuleService moduleService;

  @Autowired
  public ModuleController(ModuleService moduleService) {
    this.moduleService = moduleService;
  }

  /**
   * Fetch all the module data related to the journal entry. If the user does not own a journal
   * entry with the provided ID, the endpoint will return 404 not found.
   *
   * @param userPrincipal Authenticated user
   * @param entryId UUID of the journal entry
   * @return ModuleData containing data of all the modules related to the entry
   */
  @GetMapping("{entryId}")
  public ResponseEntity<ModuleDataDto> getModuleData(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable UUID entryId) {

    return moduleService.getModuleData(userPrincipal, entryId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("{entryId}")
  public ResponseEntity<List<Tag>> setModuleData(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody ModuleDataDto moduleData,
      @PathVariable UUID entryId) {

      moduleService.updateModuleData(userPrincipal, entryId, moduleData);

      return ResponseEntity.ok().build();
  }
}
