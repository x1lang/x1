package x1.model;

import lombok.Data;

@Data
public class ReturnStatementNode implements StatementNode {
  private ExpressionNode expression;
}
