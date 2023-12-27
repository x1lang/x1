package x1.model;

import lombok.Data;

@Data
public class FieldDeclarationNode {
  private IdentifierNode identifier;
  private TypeNode type;
}
