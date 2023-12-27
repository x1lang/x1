package x1.model;

import lombok.Value;

@Value
public class ParenExpressionNode implements ExpressionNode {
  ExpressionNode expression;
}
