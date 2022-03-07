package com.mklinga.reflekt.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

  @Value("${modules.image-directory}")
  private String imageDirectory;

  /* Only PNG files are supported for now
     TODO: Take hold of the mime type when uploading files, store in db, use everywhere.
   */
  private final String allowedFileExtension = ".png";

  public Resource getResource(Integer userId, UUID imageId) {
    String fileName = imageId.toString() + allowedFileExtension;
    Path path = Paths.get(imageDirectory, Integer.toString(userId), fileName);
    return new FileSystemResource(path);
  }
}
