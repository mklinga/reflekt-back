package com.mklinga.reflekt.services;

import com.mklinga.reflekt.dtos.JournalEntryDto;
import com.mklinga.reflekt.dtos.JournalListItemDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.NavigationData;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.repositories.JournalEntryRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
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

  @Autowired
  private JournalEntryRepository journalEntryRepository;

  @Autowired
  private ModelMapper modelMapper;

  @PersistenceContext
  EntityManager entityManager;

  public List<JournalEntry> getAllJournalEntries(UserPrincipal user) {
    Sort sort = Sort.by(Sort.Direction.DESC, "entryDate");
    return journalEntryRepository.findAllByOwner(user.getUser(), sort);
  }

  @Transactional(readOnly = true)
  public List<JournalListItemDto> getAllEntriesAsListItems(UserPrincipal user) {
    List<JournalEntry> entries = getAllJournalEntries(user);
    List<UUID> resultList = entityManager
        .createQuery(
            "SELECT e.id FROM JournalEntry e INNER JOIN ImageModule m ON m.journalEntry = e WHERE e.owner = ?1")
        .setParameter(1, user.getUser())
        .getResultList();

    return entries.stream().map(entry -> {
      JournalListItemDto listItem = modelMapper.map(entry, JournalListItemDto.class);
      listItem.setHasImages(resultList.contains(listItem.getId()));
      return listItem;
    }).collect(Collectors.toList());
  }

  public Optional<JournalEntry> getJournalEntry(UserPrincipal user, UUID uuid) {
    return journalEntryRepository.findByOwnerAndId(user.getUser(), uuid);
  }

  public NavigationData getEntryNavigationData(UserPrincipal user, UUID entryId) {
    Object[] result = (Object[]) entityManager.createNativeQuery(
            new StringBuilder().append("WITH e AS (")
                .append(" SELECT id,")
                .append(" lag(id) OVER (ORDER BY entry_date) AS previous,")
                .append(" lead(id) OVER (ORDER BY entry_date) AS next")
                .append(" FROM entries WHERE owner = ?1 ORDER BY entry_date ASC")
                .append(" )")
                .append(" SELECT cast(id as varchar),")
                .append(" cast(previous as varchar),")
                .append(" cast(next as varchar)")
                .append(" FROM e WHERE e.id = ?2").toString())
        .setParameter(1, user.getUser().getId())
        .setParameter(2, entryId)
        .getSingleResult();

    NavigationData navigationData = new NavigationData();
    if (result == null || result.length < 3) {
      return navigationData;
    }

    if (result[1] != null) {
      navigationData.setPrevious(UUID.fromString((String)result[1]));
    }

    if (result[2] != null) {
      navigationData.setNext(UUID.fromString((String)result[2]));
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
}
