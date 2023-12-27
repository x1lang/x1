package x1.model;

import lombok.Value;

@Value
public class TypeNode {
  IdentifierNode identifier;
  boolean array;
}
