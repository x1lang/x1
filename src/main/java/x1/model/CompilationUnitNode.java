package x1.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompilationUnitNode {
  private PackageNode packageStatement;
  private final List<ImportNode> importStatements = new ArrayList<>();
  private final List<TypeDeclarationNode> typeDeclarations = new ArrayList<>();
  private final List<MethodDeclarationNode> methodDeclarations = new ArrayList<>();

  public void addImportStatement(ImportNode importStatement) {
    this.importStatements.add(importStatement);
  }

  public void addTypeDeclaration(TypeDeclarationNode typeDeclaration) {
    this.typeDeclarations.add(typeDeclaration);
  }

  public void addMethodDeclaration(MethodDeclarationNode methodDeclaration) {
    this.methodDeclarations.add(methodDeclaration);
  }

}