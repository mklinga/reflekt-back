package com.mklinga.reflekt.common.model;

import lombok.Data;

/**
 * Data structure for a generic, navigable data response.
 *
 * @param <T> Type of the contained data
 */
@Data
public class Navigable<T> {
  private T data;
  private NavigationData navigationData;

  public Navigable(T data, NavigationData navigationData) {
    this.data = data;
    this.navigationData = navigationData;
  }
}
