package x1.model;

import lombok.Data;

@Data
public class IdentifierNode implements ExpressionNode {
  private Token token;
}
