package com.mklinga.reflekt.contacts.interfaces;

import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JobInformation;

public interface DraftableContact {
  FullName getFullName();

  JobInformation getJobInformation();

  String getDescription();
}
