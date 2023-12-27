package x1.model;

import lombok.Data;

@Data public class UnaryExpressionNode implements ExpressionNode {
  private UnaryOperatorNode operator;
  private ExpressionNode expression;
}
