package x1.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CompilationUnitNode {
  private final List<TypeDeclarationNode> typeDeclarations = new ArrayList<>();
  private final List<MethodDeclarationNode> methodDeclarations = new ArrayList<>();

  public void addTypeDeclaration(TypeDeclarationNode typeDeclaration) {
    this.typeDeclarations.add(typeDeclaration);
  }

  public void addMethodDeclaration(MethodDeclarationNode methodDeclaration) {
    this.methodDeclarations.add(methodDeclaration);
  }
}
