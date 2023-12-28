package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class VariableDeclarationNode implements StatementNode {
  IdentifierNode identifier;
  TypeNode type;
  ExpressionNode expression;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
