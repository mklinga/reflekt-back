package com.mklinga.reflekt.journal.services;

import com.mklinga.reflekt.journal.model.JournalEntry;
import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.model.Image;
import com.mklinga.reflekt.journal.repositories.ImageRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles the interaction with the Image repository, adding/removing/fetching image names
 * related to the JournalEntry.
 */
@Service
public class ImageService {
  private ImageRepository imageRepository;

  @Autowired
  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public List<Image> getAllImagesForEntry(User user, JournalEntry journalEntry) {
    return imageRepository
        .findByJournalEntryAndOwnerAndDeleted(journalEntry, user, false);
  }

  /**
   * Soft-delete the image in the repository (set deleted - flag to true).
   *
   * @param user logged-in user
   * @param imageId UUID of the image
   * @return deleted image if found
   */
  public Optional<Image> deleteImage(User user, UUID imageId) {
    return imageRepository
        .findByOwnerAndId(user, imageId)
        .map(image -> {
          image.setDeleted(true);
          return Optional.of(imageRepository.save(image));
        })
        .orElse(Optional.empty());
  }

  /**
   * Save new image information into the imageRepository.
   *
   * @param user Logged-in user
   * @param journalEntry Journal entry in which the image is related to
   * @param imageName Original filename of the image
   * @return newly saved image
   */
  public Image saveNewImage(User user, JournalEntry journalEntry, String imageName) {
    Image image = new Image();
    image.setJournalEntry(journalEntry);
    image.setImageName(imageName);
    image.setOwner(user);
    image.setDeleted(false);
    // TODO: Allow other mime types
    image.setMimeType("image/jpeg");
    return imageRepository.save(image);
  }
}
