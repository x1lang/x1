package x1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Stack;
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

  /**
   * Parse the expression using the shunting-yard algorithm which is a method for parsing
   * mathematical expressions specified in infix notation.
   */
  private ExpressionNode expression() throws IOException {
    Stack<ExpressionNode> outputQueue = new Stack<>();
    Stack<Token> operatorStack = new Stack<>();

    while (isOperand(currentToken)
        || isOperator(currentToken)
        || currentToken.getType() == TokenType.LPAREN
        || currentToken.getType() == TokenType.LBRACKET) {

      if (isOperand(currentToken)) {
        if (currentToken.getType() == TokenType.IDENTIFIER) {
          outputQueue.push(new IdentifierNode(currentToken));
        } else {
          outputQueue.push(new LiteralNode(currentToken));
        }
        currentToken = lexer.nextToken();
        // is it an array creation
      } else if (currentToken.getType() == TokenType.LBRACKET) {
        TypeNode type = type();
        match(TokenType.LBRACKET);
        List<ExpressionNode> expressions = new ArrayList<>();
        expressions.add(expression());
        while (peek() == TokenType.COMMA) {
          match(TokenType.COMMA);
          expressions.add(expression());
        }
        match(TokenType.RBRACKET);
        outputQueue.push(new ArrayExpressionNode(type, expressions));
      } else if (isOperator(currentToken)) {
        while (!operatorStack.isEmpty() && hasPrecedence(currentToken, operatorStack.peek())) {
          Token operator = operatorStack.pop();
          ExpressionNode right = outputQueue.pop();
          ExpressionNode left = outputQueue.pop();
          outputQueue.push(new BinaryExpressionNode(left, new BinaryOperatorNode(operator), right));
        }
        operatorStack.push(currentToken);
        currentToken = lexer.nextToken();
      } else if (currentToken.getType() == TokenType.LPAREN) {
        operatorStack.push(currentToken);
        currentToken = lexer.nextToken();
      } else if (currentToken.getType() == TokenType.RPAREN) {
        while (operatorStack.peek().getType() != TokenType.LPAREN) {
          Token operator = operatorStack.pop();
          ExpressionNode right = outputQueue.pop();
          ExpressionNode left = outputQueue.pop();
          outputQueue.push(new BinaryExpressionNode(left, new BinaryOperatorNode(operator), right));
        }
        Token token = operatorStack.pop(); // Discard the left parenthesis
        if (token.getType() != TokenType.LPAREN) {
          throw new RuntimeException("Invalid token: " + token);
        }
        currentToken = lexer.nextToken();
      }
    }
    while (!operatorStack.isEmpty()) {
      Token operator = operatorStack.pop();
      ExpressionNode right = outputQueue.pop();
      ExpressionNode left = outputQueue.pop();
      outputQueue.push(new BinaryExpressionNode(left, new BinaryOperatorNode(operator), right));
    }

    return outputQueue.pop();
  }

  private boolean isOperand(Token token) {
    return EnumSet.of(TokenType.NUMBER, TokenType.IDENTIFIER, TokenType.TRUE, TokenType.FALSE)
        .contains(token.getType());
  }

  private boolean isOperator(Token token) {
    return EnumSet.of(
            TokenType.PLUS,
            TokenType.MINUS,
            TokenType.MULTIPLY,
            TokenType.DIVIDE,
            TokenType.AND,
            TokenType.OR,
            TokenType.MODULO,
            TokenType.EQUAL_EQUAL,
            TokenType.NOT_EQUAL,
            TokenType.LESS,
            TokenType.LESS_EQUAL,
            TokenType.GREATER,
            TokenType.GREATER_EQUAL)
        .contains(token.getType());
  }

  private boolean hasPrecedence(Token token1, Token token2) {
    if (token2.getType() == TokenType.LPAREN || token2.getType() == TokenType.RPAREN) {
      return false;
    }
    return (token1.getType() != TokenType.MULTIPLY && token1.getType() != TokenType.DIVIDE)
        || (token2.getType() != TokenType.PLUS && token2.getType() != TokenType.MINUS);
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
