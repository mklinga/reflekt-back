package com.mklinga.reflekt.controllers;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.services.StorageService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {
  private final StorageService storageService;

  @Autowired
  public ImageController(StorageService storageService) {
    this.storageService = storageService;
  }

  @GetMapping("/{imageId}")
  public ResponseEntity<Resource> getImage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @PathVariable final UUID imageId) {

    Resource image = storageService.getResource(userPrincipal.getUser().getId(), imageId);
    return ResponseEntity.ok().header(CONTENT_TYPE, "image/png").body(image);
  }
}
