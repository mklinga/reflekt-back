package com.mklinga.reflekt.dtos;

import com.mklinga.reflekt.model.NavigationData;
import lombok.Data;

@Data
public class Navigable<T> {
  private T data;
  private NavigationData navigationData;

  public Navigable(T data, NavigationData navigationData) {
    this.data = data;
    this.navigationData = navigationData;
  }
}
