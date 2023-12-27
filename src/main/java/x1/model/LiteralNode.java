package x1.model;

import lombok.Value;

@Value
public class LiteralNode implements ExpressionNode {
  Token token;
}
