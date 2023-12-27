package x1.model;

import lombok.Data;

@Data
public class VariableDeclarationNode implements StatementNode {
  private IdentifierNode identifier;
  private TypeNode type;
  private ExpressionNode expression;
}
