package com.mklinga.reflekt.exceptions;

/**
 * StorageException may be thrown when persisting data into a storage does not succeed.
 */
public class StorageException extends RuntimeException {
  public StorageException(String message) {
    super(message);
  }
}
