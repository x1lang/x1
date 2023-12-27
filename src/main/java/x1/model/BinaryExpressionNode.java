package x1.model;

import lombok.Value;

@Value
public class BinaryExpressionNode implements ExpressionNode {
  ExpressionNode leftExpression;
  BinaryOperatorNode operator;
  ExpressionNode rightExpression;
}
