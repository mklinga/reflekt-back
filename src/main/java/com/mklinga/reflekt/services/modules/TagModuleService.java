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
}
