package com.mklinga.reflekt.services.modules;

import com.mklinga.reflekt.dtos.ImageModuleDataDto;
import com.mklinga.reflekt.dtos.ModuleDataDto;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.services.JournalEntryService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Main Service to handle all the module-related data fetching and manipulation. Uses individual
 * module-specific services for handling the tasks.
 */
@Service
public class ModuleService {
  private final JournalEntryService journalEntryService;
  private final ImageModuleService imageModuleService;

  @Autowired
  public ModuleService(JournalEntryService journalEntryService,
                       ImageModuleService imageModuleService) {
    this.journalEntryService = journalEntryService;
    this.imageModuleService = imageModuleService;
  }

  /**
   * Fetches data from all the modules related to the JournalEntry. If the entry cannot be found
   * (or is not owned by the authenticated user), the method will return Optional.empty().
   *
   * @param userPrincipal Authenticated user
   * @param entryId       UUID of the journal entry
   * @return Data from all the modules related to the JournalEntry
   */
  public Optional<ModuleDataDto> getModuleData(UserPrincipal userPrincipal, UUID entryId) {
    return journalEntryService
        .getJournalEntry(userPrincipal, entryId).map(entry -> {
          List<ImageModuleDataDto> images = imageModuleService
              .getAllImagesForEntry(userPrincipal.getUser(), entry)
              .stream()
              .map(ImageModuleDataDto::of)
              .collect(Collectors.toList());

          ModuleDataDto moduleData = new ModuleDataDto();
          moduleData.setImages(images);
          return moduleData;
        });
  }
}
