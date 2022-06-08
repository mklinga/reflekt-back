package com.mklinga.reflekt.services;

import com.mklinga.reflekt.dtos.JournalEntryDto;
import com.mklinga.reflekt.dtos.JournalListItemDto;
import com.mklinga.reflekt.dtos.SearchResultDto;
import com.mklinga.reflekt.dtos.TagModuleDataDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.NavigationData;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.model.modules.Tag;
import com.mklinga.reflekt.repositories.JournalEntryRepository;
import com.mklinga.reflekt.services.modules.TagModuleService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Service to manipulate journal entries.
 */
@Service
public class JournalEntryService {

  private final JournalEntryRepository journalEntryRepository;
  private final TagModuleService tagModuleService;
  private final ModelMapper modelMapper;

  /**
   * JournalEntryService deals with all the manipulation/fetching of the journal entries.
   *
   * @param journalEntryRepository Database handler for journal entries
   * @param modelMapper Mapper that is used in converting between models and DTOs
   * @param tagModuleService Service that deals with the tags
   */
  @Autowired
  public JournalEntryService(JournalEntryRepository journalEntryRepository,
                             ModelMapper modelMapper,
                             TagModuleService tagModuleService) {
    this.journalEntryRepository = journalEntryRepository;
    this.tagModuleService = tagModuleService;
    this.modelMapper = modelMapper;
  }

  @PersistenceContext
  EntityManager entityManager;

  private List<JournalEntry> getAllJournalEntries(UserPrincipal user) {
    Sort sort = Sort.by(Sort.Direction.DESC, "entryDate");
    return journalEntryRepository.findAllByOwner(user.getUser(), sort);
  }

  private List<JournalEntry> getFilteredJournalEntries(UserPrincipal user, String search) {
    Sort sort = Sort.by(Sort.Direction.DESC, "entryDate");
    return journalEntryRepository
        .findAllByOwnerAndEntryContainingIgnoreCase(user.getUser(), search, sort);
  }

  /**
   * Retrieves a list of all the journal entries in "listitem" format. This format is used in the
   * main /journal view of the application and doesn't need all the data from the items (most
   * notably, the actual entry text is omitted).
   *
   * @param user Authenticated user
   * @param search Possible search text
   * @return List of all items, if search is null, or a filtered list if search is enabled
   */
  @Transactional(readOnly = true)
  public List<JournalListItemDto> getAllEntriesAsListItems(UserPrincipal user, String search) {
    List<JournalEntry> entries = (search == null)
        ? getAllJournalEntries(user)
        : getFilteredJournalEntries(user, search);
    List<UUID> entriesWithImages = entityManager
        .createNamedQuery("JournalEntry_GetEntriesWithImages")
        .setParameter("owner", user.getUser())
        .getResultList();

    Map<UUID, List<Tag>> tagsByEntryId = tagModuleService
        .getMapOfTagsByEntryIdForUser(user.getUser());

    return entries.stream().map(entry -> {
      JournalListItemDto listItem = modelMapper.map(entry, JournalListItemDto.class);
      listItem.setHasImages(entriesWithImages.contains(listItem.getId()));
      if (tagsByEntryId.containsKey(entry.getId())) {
        listItem.setTags(tagsByEntryId
            .get(entry.getId())
            .stream()
            .sorted(Comparator.comparing(a -> a.getColor().toLowerCase(Locale.ROOT)))
            .map(tag -> modelMapper.map(tag, TagModuleDataDto.class))
            .collect(Collectors.toList()));
      }

      return listItem;
    }).collect(Collectors.toList());
  }

  public Optional<JournalEntry> getJournalEntry(UserPrincipal user, UUID uuid) {
    return journalEntryRepository.findByOwnerAndId(user.getUser(), uuid);
  }

  /**
   * Returns the navigation data (next/previous) to the JournalEntry.
   *
   * @param user Authenticated user
   * @param entryId ID of the journal entry
   * @return NavigationData
   */
  public NavigationData getEntryNavigationData(UserPrincipal user, UUID entryId) {
    Object[] result = (Object[]) entityManager
        .createNamedQuery("JournalEntry_GetNavigationData")
        .setParameter("owner", user.getUser().getId())
        .setParameter("id", entryId)
        .getSingleResult();

    NavigationData navigationData = new NavigationData();
    if (result == null || result.length < 3) {
      return navigationData;
    }

    if (result[1] != null) {
      navigationData.setPrevious(UUID.fromString((String) result[1]));
    }

    if (result[2] != null) {
      navigationData.setNext(UUID.fromString((String) result[2]));
    }

    return navigationData;
  }

  /**
   * Create new Journal entry into the database.
   *
   * @param journalEntryDto new JournalEntry
   * @return saved JournalEntry
   */

  public JournalEntry addJournalEntry(UserPrincipal user, JournalEntryDto journalEntryDto) {
    JournalEntry journalEntry = modelMapper.map(journalEntryDto, JournalEntry.class);
    journalEntry.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
    journalEntry.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
    journalEntry.setOwner(user.getUser());

    return journalEntryRepository.save(journalEntry);
  }

  /**
   * Update existing JournalEntry in the database.
   *
   * @param uuid            UUID of the existing item
   * @param journalEntryDto Updated item
   * @return Updated item
   */
  public Optional<JournalEntry> updateJournalEntry(UserPrincipal user, UUID uuid,
                                                   JournalEntryDto journalEntryDto) {
    return journalEntryRepository
        .findByOwnerAndId(user.getUser(), uuid)
        .map(original -> {
          JournalEntry journalEntry = modelMapper.map(journalEntryDto, JournalEntry.class);
          journalEntry.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
          journalEntry.setOwner(user.getUser());

          return journalEntryRepository.save(journalEntry);
        });
  }

  /**
   * Delete existing JournalEntry by UUID.
   *
   * @param uuid UUID to delete
   * @return true when successful, false when item not found
   */
  public boolean deleteJournalEntry(UserPrincipal user, UUID uuid) {
    if (journalEntryRepository.findByOwnerAndId(user.getUser(), uuid).isEmpty()) {
      return false;
    }

    journalEntryRepository.deleteById(uuid);
    return true;
  }

  /**
   * Returns a list of journal entries formatted as SearchResultDto based on the query text or a
   * tag name.
   *
   * @param user Authenticated user
   * @param query Query text (can be null)
   * @param tag Tag name (can be null)
   * @return List of matching search results
   */
  @Transactional(readOnly = true)
  public Optional<List<SearchResultDto>> getSearchResults(User user, String query, String tag) {
    if (query != null && tag != null) {
      /* TODO: searching for both */
      return Optional.empty();
    }

    if (query != null) {
      Sort sort = Sort.by(Sort.Direction.DESC, "entryDate");
      List<JournalEntry> entries = journalEntryRepository
          .findAllByOwnerAndEntryContainingIgnoreCase(user, query, sort);
      return Optional.of(
          entries
          .stream()
          .map(entry -> modelMapper.map(entry, SearchResultDto.class))
          .collect(Collectors.toList()));
    }

    if (tag != null) {
      /* TODO: tag search */
    }

    return Optional.empty();
  }
}
