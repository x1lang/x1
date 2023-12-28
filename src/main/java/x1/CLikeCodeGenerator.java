package x1;

import x1.model.*;

abstract class CLikeCodeGenerator implements NodeVisitor, CodeGenerator {
  private final StringBuilder builder = new StringBuilder();
  int indent = 0;

  @Override
  public String generate(Node node) {
    node.accept(this);
    return builder.toString();
  }

  void indent() {
    for (int i = 0; i < indent; i++) {
      builder.append("  "); // Two spaces for each indent level
    }
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
  public void visit(ParameterListNode node) {
    for (int i = 0; i < node.getParameters().size(); i++) {
      if (i > 0) {
        append(", ");
      }
      node.getParameters().get(i).accept(this);
    }
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
  public void visit(ReturnStatementNode node) {
    append("return ");
    node.getExpression().accept(this);
  }

  @Override
  public void visit(ExpressionNode node) {
    node.accept(this);
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
  public void visit(BinaryExpressionNode node) {
    node.getLeftExpression().accept(this);
    append(" ");
    node.getOperator().accept(this);
    append(" ");
    node.getRightExpression().accept(this);
  }

  @Override
  public void visit(BinaryOperatorNode node) {
    append(node.getToken().getText());
  }

  @Override
  public void visit(IdentifierNode node) {
    append(node.getToken().getText());
  }

  @Override
  public void visit(LiteralNode node) {
    append(node.getToken().getText());
  }

  abstract IdentifierNode typeIdentifier(IdentifierNode identifier);

  abstract String type(String text);

  abstract TypeNode type(TypeNode type);

  void append(String text) {
    builder.append(text);
  }
}
