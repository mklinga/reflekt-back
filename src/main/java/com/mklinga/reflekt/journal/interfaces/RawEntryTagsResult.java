package com.mklinga.reflekt.journal.interfaces;

import java.util.UUID;

public interface RawEntryTagsResult {
  UUID getEntryId();

  UUID getTagId();

  String getName();

  String getColor();
}
