package x1;

import x1.model.*;

public class JavaCodeGenerator extends CLikeCodeGenerator {

  @Override
  public String getLanguage() {
    return "java";
  }

  @Override
  public String getExtension() {
    return "java";
  }

  @Override
  IdentifierNode typeIdentifier(IdentifierNode identifier) {
    String text = type(identifier.getToken().getText());
    return new IdentifierNode(new Token(TokenType.IDENTIFIER, text));
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
  TypeNode type(TypeNode type) {
    return new TypeNode(typeIdentifier(type.getIdentifier()), type.isArray());
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
              append(";\n");
            });
    indent();
    indent();
    append("}");
  }

  @Override
  public void visit(FieldDeclarationNode node) {
    type(node.getType()).accept(this);
    append(" ");
    node.getIdentifier().accept(this);
    append(";\n");
  }

  @Override
  public void visit(MethodDeclarationNode node) {
    append("public ");
    type(node.getType()).accept(this);
    append(" ");
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
    node.getType().accept(this);
    append(" ");
    node.getIdentifier().accept(this);
  }

  @Override
  public void visit(TypeNode node) {
    typeIdentifier(node.getIdentifier()).accept(this);
    if (node.isArray()) {
      append("[]");
    }
  }

  @Override
  public void visit(ForEachDeclarationNode node) {
    append("for (");
    node.getType().accept(this);
    append(" ");
    node.getIdentifier().accept(this);
    append(" : ");
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
    type(node.getType()).accept(this);
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
  public void visit(ArrayExpressionNode node) {
    append("new ");
    type(node.getType()).accept(this);
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
