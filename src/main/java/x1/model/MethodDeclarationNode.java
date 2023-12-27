package x1.model;

import lombok.Data;

@Data
public class MethodDeclarationNode {
  private IdentifierNode identifier;
  private ParameterListNode parameterList;
  private TypeNode type;
  private BlockNode block;
}
