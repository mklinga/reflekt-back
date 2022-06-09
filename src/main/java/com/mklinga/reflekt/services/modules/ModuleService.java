package com.mklinga.reflekt.services.modules;

import com.mklinga.reflekt.dtos.ImageDataDto;
import com.mklinga.reflekt.dtos.ModuleDataDto;
import com.mklinga.reflekt.dtos.TagDataDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.model.Tag;
import com.mklinga.reflekt.services.ImageService;
import com.mklinga.reflekt.services.JournalEntryService;
import com.mklinga.reflekt.services.TagService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Main Service to handle all the module-related data fetching and manipulation. Uses individual
 * module-specific services for handling the tasks.
 */
@Service
public class ModuleService {
  private final JournalEntryService journalEntryService;
  private final ImageService imageService;
  private final TagService tagService;

  private final ModelMapper modelMapper;

  @Autowired
  public ModuleService(JournalEntryService journalEntryService,
                       ImageService imageService,
                       TagService tagService,
                       ModelMapper modelMapper) {
    this.journalEntryService = journalEntryService;
    this.imageService = imageService;
    this.tagService = tagService;
    this.modelMapper = modelMapper;
  }

  private List<ImageDataDto> getImageModuleData(User user, JournalEntry entry) {
    return imageService
        .getAllImagesForEntry(user, entry)
        .stream()
        .map(image -> modelMapper.map(image, ImageDataDto.class))
        .collect(Collectors.toList());
  }

  private List<TagDataDto> getTagModuleData(User user, JournalEntry entry) {
    return tagService
        .getTagsForEntry(user, entry)
        .stream()
        .map(tag -> modelMapper.map(tag, TagDataDto.class))
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

  @Transactional(readOnly = false)
  public void updateModuleData(
      UserPrincipal userPrincipal, UUID entryId, ModuleDataDto moduleData) {

    List<TagDataDto> tagsDto = moduleData.getTags();

    journalEntryService
        .getJournalEntry(userPrincipal, entryId).map(entry -> {
          if (tagsDto != null) {
            List<Tag> tags = tagsDto.stream()
                .map(tagDto -> modelMapper.map(tagDto, Tag.class))
                .collect(Collectors.toList());

            tagService.updateTags(userPrincipal.getUser(), entry, tags);
          }
          return null;
        });
  }
}
