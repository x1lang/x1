package x1;

import x1.model.*;

public class JavaCodeGenerator implements NodeVisitor {
  private final StringBuilder builder = new StringBuilder();

  public String generate(Node node) {
    node.accept(this);
    return builder.toString();
  }

  private static IdentifierNode javaTypeIdentifier(IdentifierNode identifier) {
    String text = javaType(identifier.getToken().getText());
    return new IdentifierNode(new Token(TokenType.IDENTIFIER, text));
  }

  private static String javaType(String text) {
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

  private TypeNode javaType(TypeNode type) {
    return new TypeNode(javaTypeIdentifier(type.getIdentifier()), type.isArray());
  }

  @Override
  public void visit(CompilationUnitNode node) {
    node.getTypeDeclarations().forEach(typeDeclaration -> typeDeclaration.accept(this));
    node.getMethodDeclarations().forEach(methodDeclaration -> methodDeclaration.accept(this));
  }

  @Override
  public void visit(TypeDeclarationNode node) {
    append("class ");
    node.getIdentifier().accept(this);
    append(" {\n");
    node.getFieldDeclarations().forEach(fieldDeclaration -> fieldDeclaration.accept(this));
    append("}\n");
  }

  @Override
  public void visit(FieldDeclarationNode node) {
    append("    ");
    javaType(node.getType()).accept(this);
    append(" ");
    node.getIdentifier().accept(this);
    append(";\n");
  }

  @Override
  public void visit(MethodDeclarationNode node) {
    append("public ");
    javaType(node.getType()).accept(this);
    append(" ");
    node.getIdentifier().accept(this);
    append("(");
    node.getParameterList().accept(this);
    append(") {\n");
    node.getBlock().accept(this);
    append("}\n");
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
    node.getType().accept(this);
    append(" ");
    node.getIdentifier().accept(this);
  }

  @Override
  public void visit(TypeNode node) {
    javaTypeIdentifier(node.getIdentifier()).accept(this);
    if (node.isArray()) {
      append("[]");
    }
  }

  @Override
  public void visit(BlockNode node) {
    node.getStatements().forEach(statement -> statement.accept(this));
  }

  @Override
  public void visit(StatementNode node) {
    if (node instanceof ForDeclarationNode) {
      visit((ForDeclarationNode) node);
    } else if (node instanceof ForEachStatementNode) {
      visit((ForEachStatementNode) node);
    } else if (node instanceof VariableDeclarationNode) {
      visit((VariableDeclarationNode) node);
    } else if (node instanceof AssignmentStatementNode) {
      visit((AssignmentStatementNode) node);
    } else if (node instanceof IfStatementNode) {
      visit((IfStatementNode) node);
    } else if (node instanceof ReturnStatementNode) {
      visit((ReturnStatementNode) node);
    } else {
      throw new RuntimeException("Unknown statement node: " + node);
    }
  }

  @Override
  public void visit(ForDeclarationNode node) {
    append("for (");
    node.getVariableDeclaration().accept(this);
    node.getExpression().accept(this);
    append("; ");
    node.getAssignmentStatement().accept(this);
    append(") {\n");
    node.getBlock().accept(this);
    append("}\n");
  }

  @Override
  public void visit(ForEachStatementNode node) {
    append("for (");
    node.getType().accept(this);
    append(" ");
    node.getIdentifier().accept(this);
    append(" : ");
    node.getExpression().accept(this);
    append(") {\n");
    node.getBlock().accept(this);
    append("}\n");
  }

  @Override
  public void visit(VariableDeclarationNode node) {
    javaType(node.getType()).accept(this);
    append(" ");
    node.getIdentifier().accept(this);
    append(" = ");
    node.getExpression().accept(this);
    append(";\n");
  }

  @Override
  public void visit(AssignmentStatementNode node) {
    node.getIdentifier().accept(this);
    append(" = ");
    node.getExpression().accept(this);
    append(";\n");
  }

  @Override
  public void visit(IfStatementNode node) {
    append("if (");
    node.getExpression().accept(this);
    append(") {\n");
    node.getBlock().accept(this);
    append("}\n");
    if (node.getElseBlock() != null) {
      append("else {\n");
      node.getElseBlock().accept(this);
      append("}\n");
    }
  }

  @Override
  public void visit(ReturnStatementNode node) {
    append("return ");
    node.getExpression().accept(this);
    append(";\n");
  }

  @Override
  public void visit(ExpressionNode node) {
    node.accept(this);
  }

  @Override
  public void visit(ArrayExpressionNode node) {
    append("new ");
    javaType(node.getType()).accept(this);
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
    append(node.getToken().getText());
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
