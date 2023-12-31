package x1;

import x1.model.*;

public class TypeScriptCodeGenerator extends JavascriptCodeGenerator {

  @Override
  public String getLanguage() {
    return "typescript";
  }

  @Override
  public String getExtension() {
    return "ts";
  }

  @Override
  String type(String text) {
    switch (text) {
      case "Int":
        return "number";
      case "Boolean":
        return "boolean";
      case "Void":
        return "void";
    }
    return text;
  }

  @Override
  public void visit(TypeDeclarationNode node) {
    append("class ");
    node.getIdentifier().accept(this);
    append(" {\n");
    indent++;
    node.getFieldDeclarations()
        .forEach(
            fieldDeclaration -> {
              indent();
              fieldDeclaration.accept(this);
              append("\n");
            });
    node.getMethodDeclarations()
        .forEach(
            methodDeclaration -> {
              append("\n");
              methodDeclaration.accept(this);
            });
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(FieldDeclarationNode node) {
    node.getIdentifier().accept(this);
    append(": ");
    node.getType().accept(this);
  }

  @Override
  public void visit(MethodDeclarationNode methodDeclarationNode) {
    methodDeclarationNode.getIdentifier().accept(this);
    append("(");
    methodDeclarationNode.getParameterList().accept(this);
    append("): ");
    type(methodDeclarationNode.getReturnType()).accept(this);
    append(" {\n");
    indent++;
    methodDeclarationNode.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(FunctionDeclarationNode node) {
    append("function ");
    node.getIdentifier().accept(this);
    append("(");
    node.getParameterList().accept(this);
    append("): ");
    type(node.getReturnType()).accept(this);
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
    append(": ");
    type(node.getType()).accept(this);
  }

  @Override
  public void visit(BlockNode node) {
    node.getStatements()
        .forEach(
            statement -> {
              indent();
              statement.accept(this);
              if (!(statement instanceof ForDeclarationNode
                  || statement instanceof ForEachDeclarationNode
                  || statement instanceof IfStatementNode)) {
                append(";");
              }
              append("\n");
            });
  }

  @Override
  public void visit(TypeNode node) {
    typeIdentifier(node.getIdentifier()).accept(this);
    if (node.isArray()) {
      append("[]");
    }
  }

  @Override
  public void visit(VariableDeclarationNode node) {
    append("let ");
    node.getIdentifier().accept(this);
    append(": ");
    type(node.getType()).accept(this);
    append(" = ");
    node.getExpression().accept(this);
  }

  @Override
  public void visit(ArrayExpressionNode node) {
    append("[");
    for (int i = 0; i < node.getExpressions().size(); i++) {
      if (i > 0) {
        append(", ");
      }
      node.getExpressions().get(i).accept(this);
    }
    append("]");
  }
}
