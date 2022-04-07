package com.mklinga.reflekt.controllers;

import com.mklinga.reflekt.dtos.TagModuleDataDto;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.services.modules.TagModuleService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {
  private final TagModuleService tagModuleService;
  private final ModelMapper modelMapper;

  @Autowired
  public TagController(TagModuleService tagModuleService, ModelMapper modelMapper) {
    this.tagModuleService = tagModuleService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("/")
  public List<TagModuleDataDto> getAllTags(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    return tagModuleService.getAllTagsForOwner(userPrincipal.getUser()).stream()
        .map(tag -> modelMapper.map(tag, TagModuleDataDto.class))
        .collect(Collectors.toList());
  }
}
