package com.mklinga.reflekt.contacts.utils;

import com.mklinga.reflekt.contacts.business.Contact;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryContactIdResolver implements ContactIdResolver {
  private final Map<UUID, Contact> contacts = new HashMap<>();

  public InMemoryContactIdResolver(List<? extends Contact> contactList) {
    for (Contact c : contactList) {
      this.contacts.put(c.getId(), c);
    }
  }

  public Contact resolve(UUID id) {
    return this.contacts.get(id);
  }

  public void addContact(Contact contact) {
    this.contacts.put(contact.getId(), contact);
  }
}
