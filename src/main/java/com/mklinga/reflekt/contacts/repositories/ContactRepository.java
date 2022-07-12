package com.mklinga.reflekt.contacts.repositories;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.model.Contact;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, UUID> {
  public List<Contact> findAllByOwner(User owner);
}
