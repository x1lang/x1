package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class UnaryOperatorNode implements Node {
  Token token;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
