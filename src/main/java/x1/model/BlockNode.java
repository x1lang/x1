package x1.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class BlockNode {
  private final List<StatementNode> statements = new ArrayList<>();

  public void addStatement(StatementNode statement) {
    statements.add(statement);
  }
}
