package com.mklinga.reflekt.services.modules;

import com.mklinga.reflekt.dtos.TagModuleDataDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.modules.Tag;
import com.mklinga.reflekt.repositories.modules.TagModuleRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagModuleService {
  private final TagModuleRepository tagModuleRepository;

  @PersistenceContext
  EntityManager entityManager;

  private final ModelMapper modelMapper;

  @Autowired
  public TagModuleService(TagModuleRepository tagModuleRepository, ModelMapper modelMapper) {
    this.tagModuleRepository = tagModuleRepository;
    this.modelMapper = modelMapper;
  }

  public Tag addNewTag(User user, TagModuleDataDto tagModuleDataDto) {
    Tag tag = modelMapper.map(tagModuleDataDto, Tag.class);
    tag.setOwner(user);
    return tagModuleRepository.save(tag);
  }

  public List<Tag> getAllTagsForOwner(User user) {
    return tagModuleRepository.findByOwner(user);
  }

  public List<Tag> getTagsForEntry(User user, JournalEntry journalEntry) {
    return entityManager
        .createNativeQuery(
            "SELECT t.* FROM module_tag_entries e INNER JOIN module_tag_tags t ON e.tag_id = t.id WHERE entry_id = :entryId AND t.owner = :ownerId",
            Tag.class
        )
        .setParameter("entryId", journalEntry.getId())
        .setParameter("ownerId", user.getId())
        .getResultList();
  }

  public Map<UUID, List<Tag>> getMapOfTagsByEntryIdForUser(User user) {
    List<Object[]> result = entityManager
        .createNativeQuery(
            "SELECT cast(e.id as varchar) as entry_id, cast(t.id as varchar) as tag_id, t.name, t.color FROM entries e" +
                " INNER JOIN module_tag_entries m ON m.entry_id = e.id" +
                " INNER JOIN module_tag_tags t ON t.id = m.tag_id" +
                " WHERE e.owner = :owner"
        )
        .setParameter("owner", user.getId())
        .getResultList();

    Map<UUID, List<Tag>> tagsByEntryId = new HashMap<>();

    for (Object[] row : result) {
      UUID entryId = UUID.fromString((String) row[0]);
      tagsByEntryId.compute(entryId, (id, list) -> {
        List<Tag> newList = (list == null) ? new ArrayList<>() : list;

        Tag tag = new Tag();
        tag.setId(UUID.fromString((String) row[1]));
        tag.setName((String) row[2]);
        tag.setColor((String) row[3]);
        tag.setOwner(user);

        newList.add(tag);
        return newList;
      });
    }

    return tagsByEntryId;
  }

  public void clearTagsFromEntry(User user, JournalEntry journalEntry) {
    entityManager.createNativeQuery("DELETE FROM module_tag_entries e WHERE entry_id = :entryId")
        .setParameter("entryId", journalEntry.getId())
        .executeUpdate();

    entityManager.flush();
  }

  public void setTagsForEntry(User user, JournalEntry journalEntry, List<Tag> tags) {
    for (Tag tag : tags) {
      entityManager.createNativeQuery(
          "INSERT INTO module_tag_entries (tag_id, entry_id) VALUES (:tagId, :entryId)")
          .setParameter("tagId", tag.getId())
          .setParameter("entryId", journalEntry.getId())
          .executeUpdate();
    }
  }

  public List<Tag> updateTags(User user, JournalEntry journalEntry, List<Tag> tags) {
    clearTagsFromEntry(user, journalEntry);
    setTagsForEntry(user, journalEntry, tags);
    return getTagsForEntry(user, journalEntry);
  }
}
