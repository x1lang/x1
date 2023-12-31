package x1.model;

import java.util.List;
import lombok.Value;
import x1.NodeVisitor;

@Value
public class TypeDeclarationNode implements Node {
  IdentifierNode identifier;
  List<FieldDeclarationNode> fieldDeclarations;
  List<MethodDeclarationNode> methodDeclarations;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
