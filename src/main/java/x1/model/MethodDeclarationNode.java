package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class MethodDeclarationNode implements Node {
  IdentifierNode identifier;
  ParameterListNode parameterList;
  TypeNode type;
  BlockNode block;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
