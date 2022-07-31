package com.mklinga.reflekt.contacts.repositories;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.model.JpaContact;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<JpaContact, UUID> {
  List<JpaContact> findAllByOwner(User owner);

  JpaContact findByIdAndOwner(UUID id, User owner);
}
