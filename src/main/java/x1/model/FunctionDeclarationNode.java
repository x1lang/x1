package x1.model;

import lombok.Value;
import x1.NodeVisitor;

@Value
public class FunctionDeclarationNode implements Node {
  IdentifierNode identifier;
  ParameterListNode parameterList;
  TypeNode returnType;
  BlockNode block;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
