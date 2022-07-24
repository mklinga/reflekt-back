package com.mklinga.reflekt.contacts.utils;

import com.mklinga.reflekt.contacts.business.Contact;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ContactIdResolver {
  private final Map<UUID, Contact> contacts = new HashMap<>();

  public ContactIdResolver(List<? extends Contact> contactList) {
    for (Contact c : contactList) {
      this.contacts.put(c.getId(), c);
    }
  }

  public Contact resolve(UUID id) {
    return this.contacts.get(id);
  }
}
