package x1.model;

import java.util.List;
import lombok.Value;
import x1.NodeVisitor;

@Value
public class ArrayExpressionNode implements ExpressionNode {
  TypeNode type;
  List<ExpressionNode> expressions;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
