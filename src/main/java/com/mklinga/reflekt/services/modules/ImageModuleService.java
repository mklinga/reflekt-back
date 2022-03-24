package com.mklinga.reflekt.services.modules;

import com.mklinga.reflekt.model.JournalEntry;
import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.modules.ImageModule;
import com.mklinga.reflekt.repositories.modules.ImageModuleRepository;
import java.util.List;
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

  public List<ImageModule> getAllImages(JournalEntry journalEntry) {
    return imageModuleRepository.findByJournalEntry(journalEntry);
  }

  public void deleteImage(UUID imageId) {
    imageModuleRepository.deleteById(imageId);
  }

  public ImageModule saveNewImage(JournalEntry journalEntry, String imageName) {
    ImageModule image = new ImageModule();
    image.setJournalEntry(journalEntry);
    image.setImageName(imageName);
    return imageModuleRepository.save(image);
  }
}
