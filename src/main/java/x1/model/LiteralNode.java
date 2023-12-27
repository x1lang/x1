package x1.model;

import lombok.Data;

@Data
public class LiteralNode implements ExpressionNode {
  private Token token;
}
