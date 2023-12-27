package x1.model;

import lombok.Value;

@Value
public class ReturnStatementNode implements StatementNode {
  ExpressionNode expression;
}
