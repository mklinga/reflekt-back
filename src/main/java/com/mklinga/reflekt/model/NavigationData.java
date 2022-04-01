package com.mklinga.reflekt.model;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NavigationData {
  private UUID previous;
  private UUID next;
}
