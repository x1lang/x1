package x1.model;

import lombok.Value;

@Value
public class VariableDeclarationNode implements StatementNode {
  IdentifierNode identifier;
  TypeNode type;
  ExpressionNode expression;
}
