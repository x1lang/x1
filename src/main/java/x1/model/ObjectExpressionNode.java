package x1.model;

import java.util.List;
import lombok.Value;
import x1.NodeVisitor;

@Value
public class ObjectExpressionNode implements ExpressionNode {
  TypeNode type;
  List<ObjectFieldNode> fields;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
