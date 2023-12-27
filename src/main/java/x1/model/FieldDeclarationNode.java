package x1.model;

import lombok.Value;

@Value
public class FieldDeclarationNode {
  IdentifierNode identifier;
  TypeNode type;
}
