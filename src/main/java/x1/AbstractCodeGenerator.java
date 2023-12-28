package x1;

import x1.model.*;

abstract class AbstractCodeGenerator implements NodeVisitor, CodeGenerator {
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

  String type(String text) {
    return text;
  }

  IdentifierNode typeIdentifier(IdentifierNode identifier) {
    String text = type(identifier.getToken().getText());
    return new IdentifierNode(new Token(TokenType.IDENTIFIER, text));
  }

  TypeNode type(TypeNode type) {
    return new TypeNode(typeIdentifier(type.getIdentifier()), type.isArray());
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

  void append(String text) {
    builder.append(text);
  }
}
