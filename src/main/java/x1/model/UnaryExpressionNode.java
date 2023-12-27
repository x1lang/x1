package x1.model;

import lombok.Value;

@Value
public class UnaryExpressionNode implements ExpressionNode {
  UnaryOperatorNode operator;
  ExpressionNode expression;
}
