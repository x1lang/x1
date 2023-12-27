package x1;

import x1.model.*;

import java.io.IOException;

public class Parser {
  private final Lexer lexer;
  private Token currentToken;

  public Parser(Lexer lexer) throws IOException {
    this.lexer = lexer;
    this.currentToken = lexer.nextToken();
  }

  public CompilationUnitNode parse() throws IOException {
    CompilationUnitNode node = compilationUnit();
    match(TokenType.EOF);
    return node;
  }

  private CompilationUnitNode compilationUnit() throws IOException {
    CompilationUnitNode node = new CompilationUnitNode();
    if (currentToken.getType() == TokenType.PACKAGE) {
      node.setPackageStatement(packageStatement());
    }
    while (currentToken.getType() == TokenType.IMPORT) {
      node.addImportStatement(importStatement());
    }
    while (currentToken.getType() == TokenType.TYPE) {
      node.addTypeDeclaration(typeDeclaration());
    }
    while (currentToken.getType() == TokenType.FUNCTION) {
      node.addMethodDeclaration(methodDeclaration());
    }
    return node;
  }

  private PackageNode packageStatement() throws IOException {
    PackageNode node = new PackageNode();
    match(TokenType.PACKAGE);
    node.setPackageName(packageName());
    return node;
  }

  private PackageNameNode packageName() throws IOException {
    PackageNameNode node = new PackageNameNode();
    node.addIdentifier(identifier());
    while (currentToken.getType() == TokenType.DOT) {
      match(TokenType.DOT);
      node.addIdentifier(identifier());
    }
    return node;
  }

  private ImportNode importStatement() throws IOException {
    ImportNode node = new ImportNode();
    match(TokenType.IMPORT);
    node.setPackageName(packageName());
    return node;
  }

  private TypeDeclarationNode typeDeclaration() throws IOException {
    TypeDeclarationNode node = new TypeDeclarationNode();
    match(TokenType.TYPE);
    node.setIdentifier(identifier());
    match(TokenType.EQUAL);
    while (currentToken.getType() == TokenType.IDENTIFIER) {
      node.addFieldDeclaration(fieldDeclaration());
    }
    return node;
  }

  private FieldDeclarationNode fieldDeclaration() throws IOException {
    FieldDeclarationNode node = new FieldDeclarationNode();
    node.setIdentifier(identifier());
    match(TokenType.COLON);
    node.setType(type());
    return node;
  }

  private MethodDeclarationNode methodDeclaration() throws IOException {
    MethodDeclarationNode node = new MethodDeclarationNode();
    match(TokenType.FUNCTION);
    node.setIdentifier(identifier());
    match(TokenType.LPAREN);
    if (currentToken.getType() == TokenType.IDENTIFIER) {
      node.setParameterList(parameterList());
    }
    match(TokenType.RPAREN);
    if (currentToken.getType() == TokenType.COLON) {
      match(TokenType.COLON);
      node.setType(type());
    }
    node.setBlock(block());
    return node;
  }

  private ParameterListNode parameterList() throws IOException {
    ParameterListNode node = new ParameterListNode();
    node.addParameter(parameter());
    while (currentToken.getType() == TokenType.COMMA) {
      match(TokenType.COMMA);
      node.addParameter(parameter());
    }
    return node;
  }

  private ParameterNode parameter() throws IOException {
    ParameterNode node = new ParameterNode();
    node.setIdentifier(identifier());
    match(TokenType.COLON);
    node.setType(type());
    return node;
  }

  private TypeNode type() throws IOException {
    TypeNode node = new TypeNode();
    if (currentToken.getType() == TokenType.IDENTIFIER) {
      node.setIdentifier(identifier());
    } else {
      node.setToken(currentToken);
      match(currentToken.getType());
    }
    return node;
  }

  private BlockNode block() throws IOException {
    BlockNode node = new BlockNode();
    match(TokenType.LBRACE);
    while (currentToken.getType() != TokenType.RBRACE) {
      node.addStatement(statement());
    }
    match(TokenType.RBRACE);
    return node;
  }

  private StatementNode statement() throws IOException {
    switch (currentToken.getType()) {
      case VAR:
        return variableDeclaration();
      case IDENTIFIER:
        return assignmentStatement();
      case IF:
        return ifStatement();
      case FOR:
        return forStatement();
      case RETURN:
        return returnStatement();
      default:
        throw new Error("Invalid statement: " + currentToken.getType());
    }
  }

  private VariableDeclarationNode variableDeclaration() throws IOException {
    VariableDeclarationNode node = new VariableDeclarationNode();
    match(TokenType.VAR);
    node.setIdentifier(identifier());
    match(TokenType.COLON);
    node.setType(type());
    match(TokenType.EQUAL);
    node.setExpression(expression());
    return node;
  }

  private AssignmentStatementNode assignmentStatement() throws IOException {
    AssignmentStatementNode node = new AssignmentStatementNode();
    node.setIdentifier(identifier());
    match(TokenType.EQUAL);
    node.setExpression(expression());
    return node;
  }

  private IfStatementNode ifStatement() throws IOException {
    IfStatementNode node = new IfStatementNode();
    match(TokenType.IF);
    match(TokenType.LPAREN);
    node.setExpression(expression());
    match(TokenType.RPAREN);
    node.setBlock(block());
    if (currentToken.getType() == TokenType.ELSE) {
      match(TokenType.ELSE);
      node.setElseBlock(block());
    }
    return node;
  }

  private ForDeclarationNode forStatement() throws IOException {
    // for-loop
    ForDeclarationNode node = new ForDeclarationNode();
    match(TokenType.FOR);
    match(TokenType.LPAREN);
    node.setVariableDeclaration(variableDeclaration());
    match(TokenType.SEMICOLON);
    node.setExpression(expression());
    match(TokenType.SEMICOLON);
    node.setAssignmentStatement(assignmentStatement());
    match(TokenType.RPAREN);
    node.setBlock(block());
    return node;
  }

  private ReturnStatementNode returnStatement() throws IOException {
    ReturnStatementNode node = new ReturnStatementNode();
    match(TokenType.RETURN);
    if (currentToken.getType() != TokenType.SEMICOLON) {
      node.setExpression(expression());
    }
    return node;
  }

  private ExpressionNode expression() throws IOException {
    switch (currentToken.getType()) {
      case NUMBER:
      case TRUE:
      case FALSE:
      case STRING_LITERAL:
        return literal();
      case IDENTIFIER:
        return identifier();
      case LPAREN:
        ParenExpressionNode node = new ParenExpressionNode();
        match(TokenType.LPAREN);
        node.setExpression(expression());
        match(TokenType.RPAREN);
        return node;
      case MINUS:
      case NOT:
        return unaryExpression();
      default:
        return binaryExpression();
    }
  }

  private LiteralNode literal() throws IOException {
    LiteralNode node = new LiteralNode();
    node.setToken(currentToken);
    match(currentToken.getType());
    return node;
  }

  private BinaryExpressionNode binaryExpression() throws IOException {
    BinaryExpressionNode node = new BinaryExpressionNode();
    node.setOperator(binaryOperator());
    node.setLeftExpression(expression());
    node.setRightExpression(expression());
    return node;
  }

  private BinaryOperatorNode binaryOperator() throws IOException {
    BinaryOperatorNode node = new BinaryOperatorNode();
    node.setToken(currentToken);
    match(currentToken.getType());
    return node;
  }

  private UnaryExpressionNode unaryExpression() throws IOException {
    UnaryExpressionNode node = new UnaryExpressionNode();
    node.setOperator(unaryOperator());
    node.setExpression(expression());
    return node;
  }

  private UnaryOperatorNode unaryOperator() throws IOException {
    UnaryOperatorNode node = new UnaryOperatorNode();
    node.setToken(currentToken);
    match(currentToken.getType());
    return node;
  }

  private IdentifierNode identifier() throws IOException {
    IdentifierNode node = new IdentifierNode();
    node.setToken(currentToken);
    match(TokenType.IDENTIFIER);
    return node;
  }

  private void match(TokenType expected) throws IOException {
    if (currentToken.getType() == expected) {
      currentToken = lexer.nextToken();
    } else {
      throw new Error("Unexpected token: " + currentToken.getType() + " expected: " + expected + " at " + lexer.getLine() + ":" + lexer.getColumn());
    }
  }
}