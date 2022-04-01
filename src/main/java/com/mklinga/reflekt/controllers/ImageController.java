package com.mklinga.reflekt.controllers;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.mklinga.reflekt.dtos.ImageModuleDataDto;
import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.model.modules.ImageModule;
import com.mklinga.reflekt.services.JournalEntryService;
import com.mklinga.reflekt.services.StorageService;
import com.mklinga.reflekt.services.modules.ImageModuleService;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * ImageController handles fetching and saving of the images.
 */
@RestController
@RequestMapping("/images")
public class ImageController {
  private final StorageService storageService;
  private final ImageModuleService imageModuleService;
  private final JournalEntryService journalEntryService;
  private final ModelMapper modelMapper;

  @Autowired
  public ImageController(StorageService storageService, ImageModuleService imageModuleService,
                         JournalEntryService journalEntryService, ModelMapper modelMapper) {
    this.storageService = storageService;
    this.imageModuleService = imageModuleService;
    this.journalEntryService = journalEntryService;
    this.modelMapper = modelMapper;
  }

  /**
   * Fetches the image from the file system by id.
   * User can only fetch images he has uploaded himself (userId is used as directory in the disk).
   *
   * @param userPrincipal Authenticated user
   * @param imageId ID of the requested image
   * @return The image data if found and accessible, 404 if not.
   */
  @GetMapping("/{imageId}")
  public ResponseEntity<Resource> getImage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @PathVariable final UUID imageId) {

    Resource image = storageService.getResource(userPrincipal.getUser(), imageId);

    if (image.exists()) {
      /* TODO: Allow other mimetypes as well... */
      return ResponseEntity.ok().header(CONTENT_TYPE, "image/jpeg").body(image);
    }

    return ResponseEntity.notFound().build();
  }

  /**
   * Remove an image from the User. This will set the "deleted" flag in the database and move the
   * existing image into the "removed" directory in the file system.
   *
   * @param userPrincipal Logged-in user
   * @param imageId ID of the image
   * @return Deleted image or not found
   */
  @DeleteMapping("/{imageId}")
  public ResponseEntity<ImageModuleDataDto> deleteImage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable final UUID imageId) {
    boolean resourceRemoved = storageService.removeResource(userPrincipal.getUser(), imageId);
    if (!resourceRemoved) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.of(
        imageModuleService
            .deleteImage(userPrincipal.getUser(), imageId)
            .map(image -> modelMapper.map(image, ImageModuleDataDto.class))
    );
  }
  
  @PostMapping("")
  public ResponseEntity<ImageModuleDataDto> postImage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @RequestParam("file") MultipartFile file,
                                                      @RequestParam("journalEntry") UUID entryId) {

    /* TODO
      * error handling:
      * remove image from database if saving it fails
      * send a message to the client if something fails
     */
    return ResponseEntity.of(
        journalEntryService
            .getJournalEntry(userPrincipal, entryId)
            .map(journalEntry -> {
              ImageModule image = imageModuleService
                  .saveNewImage(userPrincipal.getUser(), journalEntry, file.getOriginalFilename());
              storageService.saveResource(userPrincipal.getUser(), file, image.getId());
              return modelMapper.map(image, ImageModuleDataDto.class);
        }));
  }
}
