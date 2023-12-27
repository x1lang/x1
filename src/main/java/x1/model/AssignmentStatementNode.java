package x1.model;

import lombok.Data;

@Data
public class AssignmentStatementNode implements StatementNode {
  private IdentifierNode identifier;
  private ExpressionNode expression;
}
