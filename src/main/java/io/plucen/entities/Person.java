package io.plucen.entities;

import java.util.UUID;
import lombok.Data;

@Data
public class Person {
  private final UUID id;
  private final String name;
}
