package x1;

import x1.model.*;

public class PythonCodeGenerator extends AbstractCodeGenerator {

  @Override
  public String getLanguage() {
    return "python";
  }

  @Override
  public String getExtension() {
    return "py";
  }

  @Override
  public void visit(VariableDeclarationNode node) {
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
  public void visit(IfStatementNode node) {
    append("if ");
    node.getExpression().accept(this);
    append(":\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    if (node.getElseBlock() != null) {
      indent();
      append("else:\n");
      indent++;
      node.getElseBlock().accept(this);
      indent--;
    }
  }

  @Override
  public void visit(TypeDeclarationNode node) {
    append("class ");
    node.getIdentifier().accept(this);
    append(":\n");
    indent++;
    indent();
    append("def __init__(self");
    node.getFieldDeclarations()
        .forEach(
            fieldDeclaration -> {
              append(", ");
              append(fieldDeclaration.getIdentifier().getToken().getText());
            });
    append("):\n");
    indent++;
    node.getFieldDeclarations()
        .forEach(
            fieldDeclaration -> {
              indent();
              fieldDeclaration.accept(this);
              append("\n");
            });
    indent--;
    node.getMethodDeclarations()
        .forEach(
            methodDeclaration -> {
              indent();
              methodDeclaration.accept(this);
              append("\n");
            });
    indent--;
  }

  @Override
  public void visit(MethodDeclarationNode methodDeclarationNode) {
    append("def ");
    methodDeclarationNode.getIdentifier().accept(this);
    append("(this");
    methodDeclarationNode.getParameterList().accept(this);
    append("):\n");
    indent++;
    methodDeclarationNode.getBlock().accept(this);
    indent--;
  }

  @Override
  public void visit(FieldDeclarationNode node) {
    append("self.");
    node.getIdentifier().accept(this);
    append(" = ");
    node.getIdentifier().accept(this);
  }

  @Override
  public void visit(FunctionDeclarationNode node) {
    append("def ");
    node.getIdentifier().accept(this);
    append("(");
    node.getParameterList().accept(this);
    append("):\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
  }

  @Override
  public void visit(ParameterListNode node) {
    for (int i = 0; i < node.getParameters().size(); i++) {
      if (i > 0) {
        append(", ");
      }
      node.getParameters().get(i).accept(this);
    }
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
  public void visit(ParameterNode node) {
    node.getIdentifier().accept(this);
  }

  @Override
  public void visit(TypeNode node) {
    node.getIdentifier().accept(this);
    if (node.isArray()) {
      append("[]");
    }
  }

  @Override
  public void visit(ForDeclarationNode node) {
    // convert to while loop
    node.getVariableDeclaration().accept(this);
    append("\n");
    indent();
    append("while ");
    node.getExpression().accept(this);
    append(":\n");
    indent++;
    node.getBlock().accept(this);
    indent();
    node.getAssignmentStatement().accept(this);
    indent--;
  }

  @Override
  public void visit(ForEachDeclarationNode node) {
    append("for ");
    node.getIdentifier().accept(this);
    append(" in ");
    node.getExpression().accept(this);
    append(":\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
  }

  @Override
  public void visit(ReturnStatementNode node) {
    append("return ");
    node.getExpression().accept(this);
    append("\n");
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
  public void visit(ObjectExpressionNode node) {
    append(node.getType().getIdentifier().getToken().getText());
    append("(");
    for (int i = 0; i < node.getFields().size(); i++) {
      if (i > 0) {
        append(", ");
      }
      node.getFields().get(i).accept(this);
    }
    append(")");
  }

  @Override
  public void visit(ObjectFieldNode objectFieldNode) {
    objectFieldNode.getIdentifier().accept(this);
    append(" = ");
    objectFieldNode.getExpression().accept(this);
  }
}
