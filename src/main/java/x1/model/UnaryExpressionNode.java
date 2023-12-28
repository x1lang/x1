package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class UnaryExpressionNode implements ExpressionNode {
  UnaryOperatorNode operator;
  ExpressionNode expression;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
