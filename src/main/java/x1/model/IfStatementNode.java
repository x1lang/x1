package x1.model;

import lombok.Data;

@Data
public class IfStatementNode implements StatementNode {
  private ExpressionNode expression;
  private BlockNode block;
  private BlockNode elseBlock;
}
