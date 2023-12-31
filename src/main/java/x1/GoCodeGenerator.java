package x1;

import x1.model.*;

public class GoCodeGenerator extends CLikeCodeGenerator {
  @Override
  public String getLanguage() {
    return "go";
  }

  @Override
  public String getExtension() {
    return "go";
  }

  @Override
  String type(String text) {
    switch (text) {
      case "Int":
        return "int";
      case "Boolean":
        return "boolean";
      case "String":
      case "Void":
        return "";
    }
    return text.substring(0, 1).toUpperCase() + text.substring(1);
  }

  @Override
  public void visit(TypeDeclarationNode node) {
    append("type ");
    node.getIdentifier().accept(this);
    append(" struct {\n");
    indent++;
    node.getFieldDeclarations()
        .forEach(
            fieldDeclaration -> {
              indent();
              fieldDeclaration.accept(this);
              append("\n");
            });
    indent--;
    indent();
    append("}");
    node.getMethodDeclarations()
        .forEach(
            methodDeclaration -> {
              append("\n\n");
              methodDeclaration.accept(this);
            });
  }

  @Override
  public void visit(MethodDeclarationNode methodDeclarationNode) {
    append("func (this) ");
    methodDeclarationNode.getIdentifier().accept(this);
    append("(");
    methodDeclarationNode.getParameterList().accept(this);
    append(") ");
    methodDeclarationNode.getReturnType().accept(this);
    append(" {\n");
    indent++;
    methodDeclarationNode.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(FieldDeclarationNode node) {
    typeIdentifier(node.getIdentifier()).accept(this);
    append(" ");
    node.getType().accept(this);
  }

  @Override
  public void visit(FunctionDeclarationNode node) {
    append("func ");
    node.getIdentifier().accept(this);
    append("(");
    node.getParameterList().accept(this);
    append(") ");
    node.getReturnType().accept(this);
    append(" {\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(ParameterNode node) {
    node.getIdentifier().accept(this);
    append(" ");
    node.getType().accept(this);
  }

  @Override
  public void visit(TypeNode node) {
    if (node.isArray()) {
      append("[]");
    }
    typeIdentifier(node.getIdentifier()).accept(this);
  }

  @Override
  public void visit(VariableDeclarationNode node) {
    node.getIdentifier().accept(this);
    append(" := ");
    node.getExpression().accept(this);
  }

  @Override
  public void visit(AssignmentStatementNode node) {
    node.getIdentifier().accept(this);
    append(" = ");
    node.getExpression().accept(this);
  }

  @Override
  public void visit(ForDeclarationNode node) {
    append("for ");
    node.getVariableDeclaration().accept(this);
    append("; ");
    node.getExpression().accept(this);
    append("; ");
    node.getAssignmentStatement().accept(this);
    append(" {\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(BlockNode node) {
    node.getStatements()
        .forEach(
            statement -> {
              indent();
              statement.accept(this);
              append("\n");
            });
  }

  @Override
  public void visit(IfStatementNode node) {
    indent();
    append("if ");
    node.getExpression().accept(this);
    append(" {\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    indent();
    append("}");
    if (node.getElseBlock() != null) {
      indent();
      append(" else {\n");
      indent++;
      node.getElseBlock().accept(this);
      indent--;
      indent();
      append("}");
    }
  }

  @Override
  public void visit(ForEachDeclarationNode node) {
    append("for ");
    node.getIdentifier().accept(this);
    append(" := range ");
    node.getExpression().accept(this);
    append(" {\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(ArrayExpressionNode node) {
    node.getType().accept(this);
    append("{");
    for (int i = 0; i < node.getExpressions().size(); i++) {
      if (i > 0) {
        append(", ");
      }
      node.getExpressions().get(i).accept(this);
    }
    append("}");
  }

  @Override
  public void visit(ObjectExpressionNode node) {
    append(node.getType().getIdentifier().getToken().getText());
    append("{");
    for (int i = 0; i < node.getFields().size(); i++) {
      if (i > 0) {
        append(", ");
      }
      node.getFields().get(i).accept(this);
    }
    append("}");
  }

  @Override
  public void visit(ObjectFieldNode objectFieldNode) {
    typeIdentifier(objectFieldNode.getIdentifier()).accept(this);
    append(": ");
    objectFieldNode.getExpression().accept(this);
  }
}
