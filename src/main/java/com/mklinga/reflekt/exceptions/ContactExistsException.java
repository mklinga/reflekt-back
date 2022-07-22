package com.mklinga.reflekt.exceptions;

public class ContactExistsException extends RuntimeException {
  public ContactExistsException(String message) {
    super(message);
  }
}
