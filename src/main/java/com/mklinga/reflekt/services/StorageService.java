package com.mklinga.reflekt.services;

import com.mklinga.reflekt.exceptions.StorageException;
import com.mklinga.reflekt.model.User;
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

  @Value("${modules.image-directory}")
  private String imageDirectory;

  /* Only JPEG files are supported for now
     TODO: Take hold of the mime type when uploading files, store in db, use everywhere.
   */
  private final String allowedFileExtension = ".jpg";

  /**
   * Get user-uploaded image from the disk.
   *
   * @param user    Authenticated user
   * @param imageId ID of the requested image
   * @return Resource from the file system
   */
  public Resource getResource(User user, UUID imageId) {
    String fileName = imageId.toString() + allowedFileExtension;
    Path path = Paths.get(imageDirectory, Integer.toString(user.getId()), fileName);
    return new FileSystemResource(path);
  }

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
