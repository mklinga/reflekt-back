package com.mklinga.reflekt.contacts.exceptions;

public class ContactExistsException extends RuntimeException {
  public ContactExistsException(String message) {
    super(message);
  }
}
