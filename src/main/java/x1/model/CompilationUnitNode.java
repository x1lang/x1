package x1.model;

import java.util.List;
import lombok.Value;

@Value
public class CompilationUnitNode {
  List<TypeDeclarationNode> typeDeclarations;
  List<MethodDeclarationNode> methodDeclarations;
}
