package com.mklinga.reflekt.common.model;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Navigation data structure, containing (possible) ID's for the next and previous item in the list.
 */
@Data
@NoArgsConstructor
public class NavigationData {
  private UUID previous;
  private UUID next;
}
