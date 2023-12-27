package x1.model;

import java.util.List;
import lombok.Value;

@Value
public class BlockNode {
  List<StatementNode> statements;
}
