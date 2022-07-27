package com.mklinga.reflekt.contacts.model;

public enum ContactEventType {
  PHONE("Phone"),
  PRESENT("Present");

  private String type;

  ContactEventType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }
}
