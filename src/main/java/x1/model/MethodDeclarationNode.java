package x1.model;

import lombok.Value;

@Value
public class MethodDeclarationNode {
  IdentifierNode identifier;
  ParameterListNode parameterList;
  TypeNode type;
  BlockNode block;
}
