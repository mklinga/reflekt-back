package com.mklinga.reflekt.services.modules;

import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.modules.ImageModule;
import com.mklinga.reflekt.repositories.modules.ImageModuleRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles the interaction with the ImageModule repository, adding/removing/fetching image names
 * related to the JournalEntry.
 */
@Service
public class ImageModuleService {
  private ImageModuleRepository imageModuleRepository;

  @Autowired
  public ImageModuleService(ImageModuleRepository imageModuleRepository) {
    this.imageModuleRepository = imageModuleRepository;
  }

  public List<ImageModule> getAllImagesForEntry(User user, JournalEntry journalEntry) {
    return imageModuleRepository
        .findByJournalEntryAndOwnerAndDeleted(journalEntry, user, false);
  }

  public Optional<ImageModule> deleteImage(User user, UUID imageId) {
    return imageModuleRepository
        .findByOwnerAndId(user, imageId)
        .map(image -> {
          image.setDeleted(true);
          return Optional.of(imageModuleRepository.save(image));
        })
        .orElse(Optional.empty());
  }

  public ImageModule saveNewImage(User user, JournalEntry journalEntry, String imageName) {
    ImageModule image = new ImageModule();
    image.setJournalEntry(journalEntry);
    image.setImageName(imageName);
    image.setOwner(user);
    image.setDeleted(false);
    // TODO: Allow other mime types
    image.setMimeType("image/jpeg");
    return imageModuleRepository.save(image);
  }
}
