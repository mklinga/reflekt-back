package com.mklinga.reflekt.repositories.modules;

import com.mklinga.reflekt.model.User;
import com.mklinga.reflekt.model.modules.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface TagModuleRepository extends CrudRepository<Tag, UUID> {
  List<Tag> findByOwner(User user);
}
