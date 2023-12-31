package x1.model;

import java.util.List;
import lombok.Value;
import x1.NodeVisitor;

@Value
public class CompilationUnitNode implements Node {
  PackageDeclarationNode packageDeclaration;
  List<TypeDeclarationNode> typeDeclarations;
  List<FunctionDeclarationNode> functionDeclarations;

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
