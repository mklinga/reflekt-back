package com.mklinga.reflekt.contacts.utils;

import com.mklinga.reflekt.contacts.business.Contact;
import java.util.UUID;

public interface ContactIdResolver {
  Contact resolve(UUID id);
}
