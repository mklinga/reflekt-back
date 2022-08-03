package com.mklinga.reflekt.contacts.model;

public enum ContactEventType {
  PHONE("PHONE"),
  IN_PERSON("IN_PERSON"),
  VIDEO_CALL("VIDEO_CALL");

  private final String type;

  ContactEventType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }
}
