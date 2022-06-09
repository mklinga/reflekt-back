package com.mklinga.reflekt.dtos;

import java.util.UUID;
import lombok.Data;

@Data
public class TagDataDto {
  private UUID id;
  private String name;
  private String color;
}
