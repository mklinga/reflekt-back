package com.mklinga.reflekt.journal.repositories;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.journal.interfaces.RawImageResult;
import com.mklinga.reflekt.journal.model.Image;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Provides interface for interacting with the Image database table.
 */
public interface ImageRepository extends CrudRepository<Image, UUID> {
  Optional<Image> findByOwnerAndId(User user, UUID id);

  @Query(nativeQuery = true,
      value = "   SELECT cast(i.id as varchar) AS id,"
              + " cast(i.journal_entry as varchar) as journalEntryId, i.image_name as imageName"
              + " FROM images i WHERE i.owner = :owner AND deleted = false")
  List<RawImageResult> findAllByOwner(@Param("owner") Integer owner);
}
