package com.mklinga.reflekt.journal.repositories;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.model.Image;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides interface for interacting with the Image database table.
 */
public interface ImageRepository extends CrudRepository<Image, UUID> {
  Optional<Image> findByOwnerAndId(User user, UUID id);
}
