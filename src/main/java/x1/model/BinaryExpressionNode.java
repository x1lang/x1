package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class BinaryExpressionNode implements ExpressionNode {
  ExpressionNode leftExpression;
  BinaryOperatorNode operator;
  ExpressionNode rightExpression;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
