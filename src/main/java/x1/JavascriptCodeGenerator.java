package x1;

import x1.model.*;

public class JavascriptCodeGenerator implements NodeVisitor, CodeGenerator {
  private final StringBuilder builder = new StringBuilder();
  private int indent = 0;

  @Override
  public String getLanguage() {
    return "javascript";
  }

  @Override
  public String getExtension() {
    return "js";
  }

  @Override
  public String generate(Node node) {
    node.accept(this);
    return builder.toString();
  }

  private void indent() {
    for (int i = 0; i < indent; i++) {
      builder.append("  "); // Two spaces for each indent level
    }
  }

  @Override
  public void visit(CompilationUnitNode node) {
    node.getTypeDeclarations().forEach(typeDeclaration -> typeDeclaration.accept(this));
    node.getMethodDeclarations().forEach(methodDeclaration -> methodDeclaration.accept(this));
  }

  @Override
  public void visit(TypeDeclarationNode node) {
    // JavaScript does not have a direct equivalent of Java's classes
  }

  @Override
  public void visit(FieldDeclarationNode node) {
    // JavaScript does not have a direct equivalent of Java's fields
  }

  @Override
  public void visit(MethodDeclarationNode node) {
    TypeNode type = node.getType();
    new TypeNode(type.getIdentifier(), type.isArray()).accept(this);
    node.getIdentifier().accept(this);
    append(" = function(");
    node.getParameterList().accept(this);
    append(") {\n");
    indent++;
    node.getBlock().accept(this);
    indent();
    append("}");
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
  public void visit(ParameterNode node) {
    node.getIdentifier().accept(this);
  }

  @Override
  public void visit(TypeNode node) {
    // JavaScript does not have a direct equivalent of Java's types
  }

  @Override
  public void visit(BlockNode node) {
    node.getStatements()
        .forEach(
            statement -> {
              indent();
              statement.accept(this);
              // not all statements need a semicolon
              if (!(statement instanceof ForDeclarationNode
                  || statement instanceof ForEachDeclarationNode
                  || statement instanceof IfStatementNode)) {
                append(";");
              }
              append("\n");
            });
  }

  @Override
  public void visit(ForDeclarationNode node) {
    append("for (");
    node.getVariableDeclaration().accept(this);
    append("; ");
    node.getExpression().accept(this);
    append("; ");
    node.getAssignmentStatement().accept(this);
    append(") {\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    indent();
    append("}");
  }

  @Override
  public void visit(ForEachDeclarationNode node) {
    append("for (let ");
    node.getIdentifier().accept(this);
    append(" of ");
    node.getExpression().accept(this);
    append(") {\n");
    node.getBlock().accept(this);
    indent();
    append("}");
  }

  @Override
  public void visit(VariableDeclarationNode node) {
    append("let ");
    TypeNode type = node.getType();
    new TypeNode(type.getIdentifier(), type.isArray()).accept(this);
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
  public void visit(IfStatementNode node) {
    append("if (");
    node.getExpression().accept(this);
    append(") {\n");
    indent++;
    node.getBlock().accept(this);
    indent--;
    indent();
    append("}");
    if (node.getElseBlock() != null) {
      append("else {\n");
      indent++;
      node.getElseBlock().accept(this);
      indent--;
      indent();
      append("}");
    }
  }

  @Override
  public void visit(ReturnStatementNode node) {
    append("return ");
    node.getExpression().accept(this);
  }

  @Override
  public void visit(ExpressionNode node) {
    node.accept(this);
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
  public void visit(LiteralNode node) {
    append(node.getToken().getText());
  }

  @Override
  public void visit(BinaryExpressionNode node) {
    node.getLeftExpression().accept(this);
    append(" ");
    node.getOperator().accept(this);
    append(" ");
    node.getRightExpression().accept(this);
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

  @Override
  public void visit(UnaryExpressionNode node) {
    node.getOperator().accept(this);
    node.getExpression().accept(this);
  }

  @Override
  public void visit(UnaryOperatorNode node) {
    append(node.getToken().getText());
  }

  @Override
  public void visit(IdentifierNode node) {
    append(node.getToken().getText());
  }

  private void append(String text) {
    builder.append(text);
  }
}
