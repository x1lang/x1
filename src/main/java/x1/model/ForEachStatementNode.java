package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class ForEachStatementNode implements StatementNode {
  IdentifierNode identifier;
  TypeNode type;
  ExpressionNode expression;
  BlockNode block;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
