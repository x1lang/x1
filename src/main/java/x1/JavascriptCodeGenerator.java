package x1;

import x1.model.*;

public class JavascriptCodeGenerator extends CLikeCodeGenerator {

  @Override
  public String getLanguage() {
    return "javascript";
  }

  @Override
  public String getExtension() {
    return "js";
  }

  @Override
  public void visit(PackageDeclarationNode packageDeclarationNode) {}

  @Override
  public void visit(TypeDeclarationNode node) {
    // class
    append("class ");
    node.getIdentifier().accept(this);
    append(" {\n");
    indent++;
    // fields
    node.getFieldDeclarations()
        .forEach(
            fieldDeclaration -> {
              indent();
              fieldDeclaration.accept(this);
              append(";\n");
            });
    // methods
    node.getMethodDeclarations()
        .forEach(
            methodDeclaration -> {
              indent();
              methodDeclaration.accept(this);
              append("\n");
            });
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(FieldDeclarationNode node) {
    node.getIdentifier().accept(this);
    append(" = ");
    node.getType().accept(this);
  }

  @Override
  public void visit(MethodDeclarationNode methodDeclarationNode) {
    methodDeclarationNode.getIdentifier().accept(this);
    append("(");
    methodDeclarationNode.getParameterList().accept(this);
    append(") {\n");
    indent++;
    methodDeclarationNode.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(FunctionDeclarationNode node) {
    node.getIdentifier().accept(this);
    append("(");
    node.getParameterList().accept(this);
    append(") {\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(ParameterNode node) {
    node.getIdentifier().accept(this);
  }

  @Override
  public void visit(TypeNode node) {
    switch (node.getIdentifier().getToken().getText()) {
      case "Int":
        append("0");
        break;
      case "String":
        append("\"\"");
        break;
      case "Boolean":
        append("false");
        break;
      default:
        throw new IllegalArgumentException("Unknown type: " + node.getIdentifier());
    }
  }

  @Override
  public void visit(ForEachDeclarationNode node) {
    append("for (let ");
    node.getIdentifier().accept(this);
    append(" of ");
    node.getExpression().accept(this);
    append(") {\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(VariableDeclarationNode node) {
    append("let ");
    append(" ");
    node.getIdentifier().accept(this);
    append(" = ");
    node.getExpression().accept(this);
  }

  @Override
  public void visit(AssignmentStatementNode node) {
    node.getIdentifier().accept(this);
    append(" = ");
    node.getExpression().accept(this);
  }

  @Override
  public void visit(ObjectExpressionNode node) {
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
    objectFieldNode.getIdentifier().accept(this);
    append(": ");
    objectFieldNode.getExpression().accept(this);
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

  @Override
  public void visit(BinaryOperatorNode node) {
    if (node.getToken().getText().equals("==")) {
      append("===");
    } else if (node.getToken().getText().equals("!=")) {
      append("!==");
    } else {
      append(node.getToken().getText());
    }
  }
}
