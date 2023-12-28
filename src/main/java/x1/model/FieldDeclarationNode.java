package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class FieldDeclarationNode implements Node {
  IdentifierNode identifier;
  TypeNode type;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
