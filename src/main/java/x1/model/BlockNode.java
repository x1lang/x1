package x1.model;

import java.util.List;
import lombok.Value;
import x1.NodeVisitor;

@Value
public class BlockNode implements Node {
  List<StatementNode> statements;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
