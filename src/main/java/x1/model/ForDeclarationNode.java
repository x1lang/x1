package x1.model;

import lombok.Value;

@Value
public class ForDeclarationNode implements StatementNode {
  VariableDeclarationNode variableDeclaration;
  ExpressionNode expression;
  AssignmentStatementNode assignmentStatement;
  BlockNode block;
}
