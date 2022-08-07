package com.mklinga.reflekt.common.model;

import lombok.Data;

/**
 * LimitedResult object is used for the controller endpoints that need to limit the amount of items
 * that are being returned.
 */
@Data
public class LimitedResult {
  private Integer page = 0;
  private Integer limit;
  private String query;
}
