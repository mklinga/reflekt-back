package com.mklinga.reflekt.repositories;

import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, UUID> {
  List<Tag> findByOwner(User user);
}
