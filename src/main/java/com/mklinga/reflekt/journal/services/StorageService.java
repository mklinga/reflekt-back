package com.mklinga.reflekt.journal.services;

import com.mklinga.reflekt.journal.exceptions.StorageException;
import com.mklinga.reflekt.authentication.model.User;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * StorageService acts as the interface to all the disk operations required by the application.
 */
@Service
public class StorageService {

  @Value("${image-directory}")
  private String imageDirectory;

  @Value("${deleted-image-directory-name}")
  private String deletedImageDirectoryName;

  /* Only JPEG files are supported for now
     TODO: Take hold of the mime type when uploading files, store in db, use everywhere.
   */
  private final String allowedFileExtension = ".jpg";

  private Path getImagePath(User user, UUID imageId) {
    String fileName = imageId.toString() + allowedFileExtension;
    return Paths.get(imageDirectory, Integer.toString(user.getId()), fileName);
  }

  private Path getDeletedImagePath(User user, UUID imageId) {
    String fileName = imageId.toString() + allowedFileExtension;
    return Paths.get(
        imageDirectory, Integer.toString(user.getId()), deletedImageDirectoryName, fileName
    );
  }

  /**
   * Get user-uploaded image from the disk.
   *
   * @param user    Authenticated user
   * @param imageId ID of the requested image
   * @return Resource from the file system
   */
  public Resource getResource(User user, UUID imageId) {
    return new FileSystemResource(getImagePath(user, imageId));
  }

  /**
   * Remove resource - functionality does not physically delete the file, but instead moves it into
   * the deleted - directory for the specific user. This is complemented by the db operation where
   * the "deleted" flag of the image will be set to TRUE.
   *
   * @param user Logged in user
   * @param imageId id of the image that is being deleted
   * @return boolean based on the success of the move operation
   */
  public boolean removeResource(User user, UUID imageId) {
    try {
      Path deletedImagesDirectory = Paths.get(
          imageDirectory, Integer.toString(user.getId()), deletedImageDirectoryName
      );

      Files.createDirectories(deletedImagesDirectory);
      Files.move(getImagePath(user, imageId), getDeletedImagePath(user, imageId));
      return true;
    } catch (IOException exception) {
      // TODO: LOG ERROR
      return false;
    }
  }

  /**
   * Save (upload) a new resource into the file system.
   *
   * @param user Authenticated user
   * @param file Resource to be saved
   * @param imageId ID of the image in the database that represents the resource
   * @return ID of the image in the database
   */
  public UUID saveResource(User user, MultipartFile file, UUID imageId) {
    if (file.isEmpty()) {
      throw new StorageException("Trying to upload an empty file");
    }

    if (!file.getOriginalFilename().toLowerCase().endsWith(allowedFileExtension)) {
      throw new StorageException("Only JPEG files are supported for now...");
    }

    Path destination = Paths.get(
        imageDirectory,
        Integer.toString(user.getId()),
        imageId.toString() + ".jpg"
    );

    try {
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
      }

      return imageId;
    } catch (IOException exception) {
      throw new StorageException("Saving the file to file system failed");
    }
  }
}
