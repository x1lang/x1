package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class ForDeclarationNode implements StatementNode {
  VariableDeclarationNode variableDeclaration;
  ExpressionNode expression;
  AssignmentStatementNode assignmentStatement;
  BlockNode block;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
