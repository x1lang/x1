package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class TypeNode implements Node {
  IdentifierNode identifier;
  boolean array;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
