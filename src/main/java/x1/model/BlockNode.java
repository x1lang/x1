package x1.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlockNode {
  private final List<StatementNode> statements = new ArrayList<>();
  public void addStatement(StatementNode statement) {
    statements.add(statement);
  }
}
