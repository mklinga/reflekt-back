package com.mklinga.reflekt.journal.controllers;

import com.mklinga.reflekt.journal.dtos.TagDataDto;
import com.mklinga.reflekt.authentication.model.UserPrincipal;
import com.mklinga.reflekt.journal.model.Tag;
import com.mklinga.reflekt.journal.services.TagService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {
  private final TagService tagService;
  private final ModelMapper modelMapper;

  @Autowired
  public TagController(TagService tagService, ModelMapper modelMapper) {
    this.tagService = tagService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("/")
  public List<TagDataDto> getAllTags(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    return tagService.getAllTagsForOwner(userPrincipal.getUser()).stream()
        .map(tag -> modelMapper.map(tag, TagDataDto.class))
        .collect(Collectors.toList());
  }

  @PostMapping("")
  public ResponseEntity<TagDataDto> addNewTag(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @RequestBody TagDataDto tag) {
    Tag newTag = tagService.addNewTag(userPrincipal.getUser(), tag);
    return ResponseEntity.ok(modelMapper.map(newTag, TagDataDto.class));
  }
}
