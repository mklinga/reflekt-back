package com.mklinga.reflekt.journal.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.authentication.model.UserPrincipal;
import com.mklinga.reflekt.common.model.LimitedResult;
import com.mklinga.reflekt.common.model.NavigationData;
import com.mklinga.reflekt.journal.dtos.JournalEntryDto;
import com.mklinga.reflekt.journal.dtos.SearchResultDto;
import com.mklinga.reflekt.journal.interfaces.RawEntryTagsResult;
import com.mklinga.reflekt.journal.interfaces.RawImageResult;
import com.mklinga.reflekt.journal.interfaces.RawJournalEntryResult;
import com.mklinga.reflekt.journal.model.Image;
import com.mklinga.reflekt.journal.model.JournalEntry;
import com.mklinga.reflekt.journal.model.Tag;
import com.mklinga.reflekt.journal.repositories.ImageRepository;
import com.mklinga.reflekt.journal.repositories.JournalEntryRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Service to manipulate journal entries.
 */
@Service
public class JournalEntryService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final JournalEntryRepository journalEntryRepository;
  private final JournalEntryMessageService journalEntryMessageService;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  /**
   * JournalEntryService deals with all the manipulation/fetching of the journal entries.
   *
   * @param journalEntryRepository     Database handler for journal entries
   * @param journalEntryMessageService Service that handler Journal entry - related messaging
   * @param modelMapper                Mapper that is used in converting between models and DTOs
   */
  @Autowired
  public JournalEntryService(JournalEntryRepository journalEntryRepository,
                             JournalEntryMessageService journalEntryMessageService,
                             ImageRepository imageRepository,
                             ModelMapper modelMapper) {
    this.journalEntryRepository = journalEntryRepository;
    this.journalEntryMessageService = journalEntryMessageService;
    this.imageRepository = imageRepository;
    this.modelMapper = modelMapper;
  }

  @PersistenceContext
  EntityManager entityManager;

  /**
   * Retrieves a list of all the journal entries in "listitem" format. This format is used in the
   * main /journal view of the application and doesn't need all the data from the items (most
   * notably, the actual entry text is omitted).
   *
   * @param user   Authenticated user
   * @param limitedResult Includes limit & page for the wanted result offset. *Note* that the query-
   *                      argument is not supported on this method.
   * @return List of all items, if search is null, or a filtered list if search is enabled
   */
  @Transactional(readOnly = true)
  public List<JournalEntryDto> getAllEntries(User user, LimitedResult limitedResult) {
    Sort sort = Sort.by(Sort.Direction.DESC, "entryDate");

    List<JournalEntry> entries;
    if (limitedResult.getLimit() == null) {
      List<RawJournalEntryResult> rawEntries = journalEntryRepository
          .findAllByOwner(user.getId());
      List<UUID> entryIds = rawEntries
          .stream().map(RawJournalEntryResult::getId).collect(Collectors.toList());

      Map<UUID, List<Tag>> allTags = journalEntryRepository
          .findTagsForEntries(entryIds)
          .stream()
          .collect(Collectors.toMap(
              RawEntryTagsResult::getEntryId,
              t -> Arrays.asList(modelMapper.map(t, Tag.class)),
              (a, b) -> Stream.concat(a.stream(), b.stream()).toList()
          ));
      Map<UUID, Set<Image>> allImages = imageRepository
          .findAllByOwner(user.getId())
          .stream()
          .collect(Collectors.toMap(
              RawImageResult::getJournalEntryId,
              i -> Set.of(modelMapper.map(i, Image.class)),
              (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toSet())
          ));

      entries = rawEntries.stream().map(rawEntry -> {
        JournalEntry entry = modelMapper.map(rawEntry, JournalEntry.class);
        entry.setTags(allTags.getOrDefault(entry.getId(), new ArrayList<>()));
        entry.setImages(allImages.getOrDefault(entry.getId(), new HashSet<>()));
        return entry;
      }).collect(Collectors.toList());
    } else {
      Pageable pageable = PageRequest.of(limitedResult.getPage(), limitedResult.getLimit(), sort);
      entries = journalEntryRepository.findAllByOwner(user, pageable);
    }

    return entries.stream()
        .map(entry -> modelMapper.map(entry, JournalEntryDto.class))
        .collect(Collectors.toList());
  }

  public Optional<JournalEntry> getJournalEntry(UserPrincipal user, UUID uuid) {
    return journalEntryRepository.findByOwnerAndId(user.getUser(), uuid);
  }

  /**
   * Returns the navigation data (next/previous) to the JournalEntry.
   *
   * @param user    Authenticated user
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

          JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
          journalEntryMessageService.sendUpdateMessage(savedEntry);

          return savedEntry;
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
   * @param user  Authenticated user
   * @param query Query text (can be null)
   * @param tag   Tag name (can be null)
   * @return List of matching search results
   */
  @Transactional(readOnly = true)
  public Optional<List<SearchResultDto>> getSearchResults(User user, String query, String tag) {
    if (query != null && tag != null) {
      /* TODO: searching for both */
      return Optional.empty();
    }

    Sort sort = Sort.by(Sort.Direction.DESC, "entryDate");
    List<JournalEntry> entries = (query != null)
        ?  journalEntryRepository.findAllByOwnerAndEntryContainingIgnoreCase(user, query, sort)
        : journalEntryRepository.findEntriesByTagName(user.getId(), tag);

    return Optional.of(
        entries
            .stream()
            .map(entry -> modelMapper.map(entry, SearchResultDto.class))
            .collect(Collectors.toList()));
  }

  /**
   * Get entry count from the database for the specific user. The COUNT() function in postgres
   * returns Long, but we will just cast it as an Integer for our purposes. (We will cross the
   * bridge where the user has more than $MAX_INTVALUE worth of entries when it's necessary...)
   *
   * @param user The authenticated user
   * @return Number of distinct entries in the db
   */
  public Integer getEntryCountByUser(User user) {
    try {
      Long count = entityManager
          .createNamedQuery("GetJournalEntryCount", Long.class)
          .setParameter("owner", user)
          .getSingleResult();

      return Math.toIntExact(count);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return 0;
    }
  }
}
