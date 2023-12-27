package x1.model;

import lombok.Value;

@Value
public class ForEachStatementNode implements StatementNode {
  IdentifierNode identifier;
  ExpressionNode expression;
  BlockNode block;
}
