package x1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import x1.model.*;

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
    List<TypeDeclarationNode> typeDeclarations = new ArrayList<>();
    List<MethodDeclarationNode> methodDeclarations = new ArrayList<>();
    while (peek() == TokenType.TYPE) {
      typeDeclarations.add(typeDeclaration());
    }
    while (peek() == TokenType.FUNCTION) {
      methodDeclarations.add(methodDeclaration());
    }
    return new CompilationUnitNode(typeDeclarations, methodDeclarations);
  }

  private TypeDeclarationNode typeDeclaration() throws IOException {
    match(TokenType.TYPE);
    IdentifierNode identifier = identifier();
    match(TokenType.LBRACE);
    List<FieldDeclarationNode> fieldDeclarations = new ArrayList<>();
    while (peek() == TokenType.IDENTIFIER) {
      fieldDeclarations.add(fieldDeclaration());
    }
    match(TokenType.RBRACE);
    return new TypeDeclarationNode(identifier, fieldDeclarations);
  }

  private FieldDeclarationNode fieldDeclaration() throws IOException {
    IdentifierNode identifier = identifier();
    match(TokenType.COLON);
    TypeNode type = type();
    return new FieldDeclarationNode(identifier, type);
  }

  private MethodDeclarationNode methodDeclaration() throws IOException {
    match(TokenType.FUNCTION);
    IdentifierNode identifier = identifier();
    match(TokenType.LPAREN);
    ParameterListNode parameterList = parameterList();
    match(TokenType.RPAREN);
    match(TokenType.COLON);
    TypeNode type = type();
    BlockNode block = block();
    return new MethodDeclarationNode(identifier, parameterList, type, block);
  }

  private ParameterListNode parameterList() throws IOException {
    List<ParameterNode> parameters = new ArrayList<>();
    if (peek() == TokenType.IDENTIFIER) {
      parameters.add(parameter());
    }
    while (peek() == TokenType.COMMA) {
      match(TokenType.COMMA);
      parameters.add(parameter());
    }
    return new ParameterListNode(parameters);
  }

  private ParameterNode parameter() throws IOException {
    IdentifierNode identifier = identifier();
    match(TokenType.COLON);
    TypeNode type = type();
    return new ParameterNode(identifier, type);
  }

  private TypeNode type() throws IOException {
    if (peek() == TokenType.LBRACKET) {
      match(TokenType.LBRACKET);
      match(TokenType.RBRACKET);
      return new TypeNode(identifier(), true);
    }
    return new TypeNode(identifier(), false);
  }

  private BlockNode block() throws IOException {
    List<StatementNode> statements = new ArrayList<>();
    match(TokenType.LBRACE);
    while (peek() != TokenType.RBRACE) {
      statements.add(statement());
    }
    match(TokenType.RBRACE);
    return new BlockNode(statements);
  }

  private StatementNode statement() throws IOException {
    switch (peek()) {
      case VAR:
        return variableDeclaration();
      case IDENTIFIER:
        return assignmentStatement();
      case IF:
        return ifStatement();
      case FOR:
        return forStatement();
      case FOREACH:
        return forEachStatement();
      case RETURN:
        return returnStatement();
      default:
        throw new Error("Invalid statement: " + peek());
    }
  }

  private StatementNode forEachStatement() throws IOException {
    match(TokenType.FOREACH);
    match(TokenType.LPAREN);
    IdentifierNode identifier = identifier();
    match(TokenType.COLON);
    TypeNode type = type();
    match(TokenType.SEMICOLON);
    ExpressionNode expression = expression();
    match(TokenType.RPAREN);
    BlockNode block = block();
    return new ForEachDeclarationNode(identifier, type, expression, block);
  }

  private VariableDeclarationNode variableDeclaration() throws IOException {
    match(TokenType.VAR);
    IdentifierNode identifier = identifier();
    match(TokenType.COLON);
    TypeNode type = type();
    match(TokenType.EQUAL);
    ExpressionNode expression = expression();
    return new VariableDeclarationNode(identifier, type, expression);
  }

  private AssignmentStatementNode assignmentStatement() throws IOException {
    IdentifierNode identifier = identifier();
    match(TokenType.EQUAL);
    ExpressionNode expression = expression();
    return new AssignmentStatementNode(identifier, expression);
  }

  private IfStatementNode ifStatement() throws IOException {
    match(TokenType.IF);
    match(TokenType.LPAREN);
    ExpressionNode expression = expression();
    match(TokenType.RPAREN);
    BlockNode block = block();
    BlockNode elseBlock = null;
    if (peek() == TokenType.ELSE) {
      match(TokenType.ELSE);
      elseBlock = block();
    }
    return new IfStatementNode(expression, block, elseBlock);
  }

  private ForDeclarationNode forStatement() throws IOException {
    match(TokenType.FOR);
    match(TokenType.LPAREN);
    VariableDeclarationNode variableDeclarationNode = variableDeclaration();
    match(TokenType.SEMICOLON);
    ExpressionNode expression = expression();
    match(TokenType.SEMICOLON);
    AssignmentStatementNode assignmentStatement = assignmentStatement();
    match(TokenType.RPAREN);
    BlockNode block = block();
    return new ForDeclarationNode(variableDeclarationNode, expression, assignmentStatement, block);
  }

  private ReturnStatementNode returnStatement() throws IOException {
    match(TokenType.RETURN);
    TokenType peek = peek();
    if (peek == TokenType.IDENTIFIER
        || peek == TokenType.NUMBER
        || peek == TokenType.TRUE
        || peek == TokenType.FALSE
        || peek == TokenType.STRING
        || peek == TokenType.MINUS
        || peek == TokenType.NOT
        || peek == TokenType.LPAREN) {
      ExpressionNode expression = expression();
      return new ReturnStatementNode(expression);
    }
    return new ReturnStatementNode(null);
  }

  private ExpressionNode expression() throws IOException {
    switch (peek()) {
      case NUMBER:
      case TRUE:
      case FALSE:
      case STRING:
        return literal();
      case IDENTIFIER:
        return identifier();
      case LBRACKET:
        return arrayExpression();
      case LPAREN:
        match(TokenType.LPAREN);
        ExpressionNode expression = expression();
        match(TokenType.RPAREN);
        return new ParenExpressionNode(expression);
      case MINUS:
      case NOT:
        return unaryExpression();
      default:
        return binaryExpression();
    }
  }

  private ExpressionNode arrayExpression() throws IOException {
    TypeNode type = type();
    match(TokenType.LBRACKET);
    List<ExpressionNode> expressions = new ArrayList<>();
    expressions.add(expression());
    while (peek() == TokenType.COMMA) {
      match(TokenType.COMMA);
      expressions.add(expression());
    }
    match(TokenType.RBRACKET);
    return new ArrayExpressionNode(type, expressions);
  }

  private LiteralNode literal() throws IOException {
    Token token = currentToken;
    match(peek());
    return new LiteralNode(token);
  }

  private BinaryExpressionNode binaryExpression() throws IOException {
    BinaryOperatorNode operator = binaryOperator();
    return new BinaryExpressionNode(expression(), operator, expression());
  }

  private BinaryOperatorNode binaryOperator() throws IOException {
    Token token = currentToken;
    match(peek());
    return new BinaryOperatorNode(token);
  }

  private UnaryExpressionNode unaryExpression() throws IOException {
    return new UnaryExpressionNode(unaryOperator(), expression());
  }

  private UnaryOperatorNode unaryOperator() throws IOException {
    Token token = currentToken;
    match(peek());
    return new UnaryOperatorNode(token);
  }

  private IdentifierNode identifier() throws IOException {
    Token token = currentToken;
    match(TokenType.IDENTIFIER);
    return new IdentifierNode(token);
  }

  private TokenType peek() {
    return currentToken.getType();
  }

  private void match(TokenType expected) throws IOException {
    if (peek() == expected) {
      currentToken = lexer.nextToken();
    } else {
      throw new IllegalStateException(
          "Unexpected token: "
              + peek()
              + " expected: "
              + expected
              + " at "
              + lexer.getLine()
              + ":"
              + lexer.getColumn());
    }
  }

  @Override
  public String toString() {
    return lexer + ":" + currentToken;
  }
}
