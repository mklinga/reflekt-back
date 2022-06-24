package com.mklinga.reflekt.journal.services;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.dtos.TagDataDto;
import com.mklinga.reflekt.journal.model.Tag;
import com.mklinga.reflekt.journal.repositories.TagRepository;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TagService handles all the tag- related actions.
 */
@Service
public class TagService {

  private final TagRepository tagRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public TagService(TagRepository tagRepository, ModelMapper modelMapper) {
    this.tagRepository = tagRepository;
    this.modelMapper = modelMapper;
  }

  /**
   * Add new tag to the database.
   *
   * @param user       The authenticated user, owner of the tag
   * @param tagDataDto Tag information
   * @return newly created Tag from the database
   */
  public Tag addNewTag(User user, TagDataDto tagDataDto) {
    Tag tag = modelMapper.map(tagDataDto, Tag.class);
    tag.setOwner(user);
    return tagRepository.save(tag);
  }

  public List<Tag> getAllTagsForOwner(User user) {
    return tagRepository.findByOwner(user);
  }
}
