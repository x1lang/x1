package x1.model;

import java.util.List;
import lombok.Value;
import x1.NodeVisitor;

@Value
public class ParameterListNode implements Node {
  List<ParameterNode> parameters;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
