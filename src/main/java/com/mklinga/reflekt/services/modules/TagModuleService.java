package com.mklinga.reflekt.services.modules;

import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.modules.Tag;
import com.mklinga.reflekt.repositories.modules.TagModuleRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagModuleService {
  private final TagModuleRepository tagModuleRepository;

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  public TagModuleService(TagModuleRepository tagModuleRepository) {
    this.tagModuleRepository = tagModuleRepository;
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
