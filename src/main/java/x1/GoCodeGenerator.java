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
      case "Void":
        return "void";
    }
    return text;
  }

  @Override
  public void visit(TypeDeclarationNode node) {
    append("type ");
    node.getIdentifier().accept(this);
    append(" struct {\n");
    indent++;
    node.getFieldDeclarations().forEach(fieldDeclaration -> fieldDeclaration.accept(this));
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(FieldDeclarationNode node) {

    node.getIdentifier().accept(this);
    append(" ");
    node.getType().accept(this);
  }

  @Override
  public void visit(MethodDeclarationNode node) {
    append("func ");
    node.getIdentifier().accept(this);
    append("(");
    node.getParameterList().accept(this);
    append(") ");
    node.getType().accept(this);
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
}
