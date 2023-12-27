package x1.model;

import lombok.Value;

@Value
public class AssignmentStatementNode implements StatementNode {
  IdentifierNode identifier;
  ExpressionNode expression;
}
