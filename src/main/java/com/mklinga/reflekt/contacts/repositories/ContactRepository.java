package com.mklinga.reflekt.contacts.repositories;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.model.JpaContact;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<JpaContact, UUID> {
  List<JpaContact> findAllByOwner(User owner);

  @Query(
      nativeQuery = true,
      value = "SELECT * FROM contacts WHERE (id IN :ids) AND (owner = :owner_id)")
  List<JpaContact> findByIdsAndOwner(@Param("ids") List<UUID> ids, @Param("owner_id") Integer userId);

  JpaContact findByIdAndOwner(UUID id, User owner);
}
