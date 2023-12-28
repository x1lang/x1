package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class IfStatementNode implements StatementNode {
  ExpressionNode expression;
  BlockNode block;
  BlockNode elseBlock;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
