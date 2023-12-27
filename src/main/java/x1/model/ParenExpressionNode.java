package x1.model;

import lombok.Data;

@Data
public class ParenExpressionNode implements ExpressionNode {
  private ExpressionNode expression;
}
