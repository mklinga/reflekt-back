package com.mklinga.reflekt.services.modules;

import com.mklinga.reflekt.dtos.ImageModuleDataDto;
import com.mklinga.reflekt.dtos.ModuleDataDto;
import com.mklinga.reflekt.dtos.TagModuleDataDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.model.modules.ImageModule;
import com.mklinga.reflekt.model.modules.Tag;
import com.mklinga.reflekt.services.JournalEntryService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
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
  private final TagModuleService tagModuleService;

  private final ModelMapper modelMapper;

  @Autowired
  public ModuleService(JournalEntryService journalEntryService,
                       ImageModuleService imageModuleService,
                       TagModuleService tagModuleService,
                       ModelMapper modelMapper) {
    this.journalEntryService = journalEntryService;
    this.imageModuleService = imageModuleService;
    this.tagModuleService = tagModuleService;
    this.modelMapper = modelMapper;
  }

  private List<ImageModuleDataDto> getImageModuleData(User user, JournalEntry entry) {
    return imageModuleService
        .getAllImagesForEntry(user, entry)
        .stream()
        .map(image -> modelMapper.map(image, ImageModuleDataDto.class))
        .collect(Collectors.toList());
  }

  private List<TagModuleDataDto> getTagModuleData(User user, JournalEntry entry) {
    return tagModuleService
        .getTagsForEntry(user, entry)
        .stream()
        .map(tag -> modelMapper.map(tag, TagModuleDataDto.class))
        .collect(Collectors.toList());
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
          ModuleDataDto moduleData = new ModuleDataDto();
          moduleData.setImages(getImageModuleData(userPrincipal.getUser(), entry));
          moduleData.setTags(getTagModuleData(userPrincipal.getUser(), entry));
          return moduleData;
        });
  }
}
