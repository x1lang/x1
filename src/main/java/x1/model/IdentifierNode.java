package x1.model;

import lombok.Value;

@Value
public class IdentifierNode implements ExpressionNode {
  Token token;
}
