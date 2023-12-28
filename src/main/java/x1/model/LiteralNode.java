package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class LiteralNode implements ExpressionNode {
  Token token;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
