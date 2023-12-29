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
  public void visit(TypeDeclarationNode node) {}

  @Override
  public void visit(FieldDeclarationNode node) {}

  @Override
  public void visit(MethodDeclarationNode node) {
    append("function ");
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
  public void visit(TypeNode node) {}

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
