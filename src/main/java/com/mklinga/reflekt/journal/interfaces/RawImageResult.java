package com.mklinga.reflekt.journal.interfaces;

import java.util.UUID;

public interface RawImageResult {
  UUID getId();

  UUID getJournalEntryId();

  String getImageName();
}
