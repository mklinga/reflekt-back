package com.mklinga.reflekt.services;

import com.mklinga.reflekt.dtos.TagDataDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.Tag;
import com.mklinga.reflekt.repositories.TagRepository;
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
public class TagService {
  private final TagRepository tagRepository;

  @PersistenceContext
  EntityManager entityManager;

  private final ModelMapper modelMapper;

  @Autowired
  public TagService(TagRepository tagRepository, ModelMapper modelMapper) {
    this.tagRepository = tagRepository;
    this.modelMapper = modelMapper;
  }

  public Tag addNewTag(User user, TagDataDto tagDataDto) {
    Tag tag = modelMapper.map(tagDataDto, Tag.class);
    tag.setOwner(user);
    return tagRepository.save(tag);
  }

  public List<Tag> getAllTagsForOwner(User user) {
    return tagRepository.findByOwner(user);
  }

  public List<Tag> getTagsForEntry(User user, JournalEntry journalEntry) {
    return entityManager
        .createNativeQuery(
            "SELECT t.* FROM tag_entries e INNER JOIN tags t ON e.tag_id = t.id WHERE entry_id = :entryId AND t.owner = :ownerId",
            Tag.class
        )
        .setParameter("entryId", journalEntry.getId())
        .setParameter("ownerId", user.getId())
        .getResultList();
  }

  public void clearTagsFromEntry(User user, JournalEntry journalEntry) {
    entityManager.createNativeQuery("DELETE FROM tag_entries e WHERE entry_id = :entryId")
        .setParameter("entryId", journalEntry.getId())
        .executeUpdate();

    entityManager.flush();
  }

  public void setTagsForEntry(User user, JournalEntry journalEntry, List<Tag> tags) {
    for (Tag tag : tags) {
      entityManager.createNativeQuery(
          "INSERT INTO tag_entries (tag_id, entry_id) VALUES (:tagId, :entryId)")
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
