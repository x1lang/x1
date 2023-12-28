package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class AssignmentStatementNode implements StatementNode {
  IdentifierNode identifier;
  ExpressionNode expression;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
