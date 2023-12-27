package x1.model;

import lombok.Value;

@Value
public class IfStatementNode implements StatementNode {
  ExpressionNode expression;
  BlockNode block;
  BlockNode elseBlock;
}
