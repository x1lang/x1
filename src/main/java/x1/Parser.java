package x1;

import static x1.model.TokenType.*;

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
    match(EOF);
    return node;
  }

  private CompilationUnitNode compilationUnit() throws IOException {
    PackageDeclarationNode packageDeclaration = packageDeclaration();
    List<TypeDeclarationNode> typeDeclarations = new ArrayList<>();
    List<FunctionDeclarationNode> functionDeclarations = new ArrayList<>();
    while (currentToken.getType() != EOF) {
      comments();
      if (currentToken.getType() == TYPE) {
        typeDeclarations.add(typeDeclaration());
      } else {
        functionDeclarations.add(functionDeclaration());
      }
    }
    return new CompilationUnitNode(packageDeclaration, typeDeclarations, functionDeclarations);
  }

  private PackageDeclarationNode packageDeclaration() throws IOException {
    if (currentToken.getType() == PACKAGE) {
      match(PACKAGE);
      IdentifierNode identifier = identifier();
      return new PackageDeclarationNode(identifier);
    }
    return new PackageDeclarationNode(new IdentifierNode(new Token(TokenType.IDENTIFIER, "")));
  }

  private TypeDeclarationNode typeDeclaration() throws IOException {
    match(TYPE);
    IdentifierNode identifier = identifier();
    match(LBRACE);
    List<FieldDeclarationNode> fieldDeclarations = new ArrayList<>();
    List<MethodDeclarationNode> methodDeclaration = new ArrayList<>();
    while (currentToken.getType() == IDENTIFIER || currentToken.getType() == COMMENT) {
      comments();
      Token lookAhead = lexer.lookAhead();
      if (lookAhead.getType() == COLON) {
        fieldDeclarations.add(fieldDeclaration());
      } else if (lookAhead.getType() == LPAREN) {
        methodDeclaration.add(methodDeclaration());
      } else {
        throw new IllegalStateException(
            "Invalid type declaration: "
                + lookAhead.getType()
                + " at "
                + lexer.getLine()
                + ":"
                + lexer.getColumn());
      }
    }
    match(RBRACE);
    return new TypeDeclarationNode(identifier, fieldDeclarations, methodDeclaration);
  }

  private FieldDeclarationNode fieldDeclaration() throws IOException {
    IdentifierNode identifier = identifier();
    match(COLON);
    TypeNode type = type();
    return new FieldDeclarationNode(identifier, type);
  }

  private MethodDeclarationNode methodDeclaration() throws IOException {
    IdentifierNode identifier = identifier();
    match(LPAREN);
    ParameterListNode parameterList = parameterList();
    match(RPAREN);
    match(COLON);
    TypeNode type = type();
    BlockNode block = block();
    return new MethodDeclarationNode(identifier, parameterList, type, block);
  }

  private FunctionDeclarationNode functionDeclaration() throws IOException {
    IdentifierNode identifier = identifier();
    match(LPAREN);
    ParameterListNode parameterList = parameterList();
    match(RPAREN);
    match(COLON);
    TypeNode type = type();
    BlockNode block = block();
    return new FunctionDeclarationNode(identifier, parameterList, type, block);
  }

  private void comments() throws IOException {
    while (currentToken.getType() == COMMENT) {
      match(COMMENT);
    }
  }

  private ParameterListNode parameterList() throws IOException {
    List<ParameterNode> parameters = new ArrayList<>();
    if (currentToken.getType() == IDENTIFIER) {
      parameters.add(parameter());
    }
    while (currentToken.getType() == COMMA) {
      match(COMMA);
      parameters.add(parameter());
    }
    return new ParameterListNode(parameters);
  }

  private ParameterNode parameter() throws IOException {
    IdentifierNode identifier = identifier();
    match(COLON);
    TypeNode type = type();
    return new ParameterNode(identifier, type);
  }

  private TypeNode type() throws IOException {
    if (currentToken.getType() == LBRACKET) {
      match(LBRACKET);
      match(RBRACKET);
      return new TypeNode(identifier(), true);
    }
    return new TypeNode(identifier(), false);
  }

  private BlockNode block() throws IOException {
    List<StatementNode> statements = new ArrayList<>();
    match(LBRACE);
    while (currentToken.getType() != RBRACE) {
      statements.add(statement());
    }
    match(RBRACE);
    return new BlockNode(statements);
  }

  private StatementNode statement() throws IOException {
    comments();
    switch (currentToken.getType()) {
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
        throw new IllegalStateException(
            "Invalid statement: "
                + currentToken.getType()
                + " at "
                + lexer.getLine()
                + ":"
                + lexer.getColumn());
    }
  }

  private StatementNode forEachStatement() throws IOException {
    match(FOREACH);
    match(LPAREN);
    IdentifierNode identifier = identifier();
    match(COLON);
    TypeNode type = type();
    match(SEMICOLON);
    ExpressionNode expression = expression();
    match(RPAREN);
    BlockNode block = block();
    return new ForEachDeclarationNode(identifier, type, expression, block);
  }

  private VariableDeclarationNode variableDeclaration() throws IOException {
    match(VAR);
    IdentifierNode identifier = identifier();
    match(COLON);
    TypeNode type = type();
    match(EQUAL);
    ExpressionNode expression = expression();
    return new VariableDeclarationNode(identifier, type, expression);
  }

  private AssignmentStatementNode assignmentStatement() throws IOException {
    IdentifierNode identifier = identifier();
    match(EQUAL);
    ExpressionNode expression = expression();
    return new AssignmentStatementNode(identifier, expression);
  }

  private IfStatementNode ifStatement() throws IOException {
    match(IF);
    match(LPAREN);
    ExpressionNode expression = expression();
    match(RPAREN);
    BlockNode block = block();
    BlockNode elseBlock = null;
    if (currentToken.getType() == ELSE) {
      match(ELSE);
      elseBlock = block();
    }
    return new IfStatementNode(expression, block, elseBlock);
  }

  private ForDeclarationNode forStatement() throws IOException {
    match(FOR);
    match(LPAREN);
    VariableDeclarationNode variableDeclarationNode = variableDeclaration();
    match(SEMICOLON);
    ExpressionNode expression = expression();
    match(SEMICOLON);
    AssignmentStatementNode assignmentStatement = assignmentStatement();
    match(RPAREN);
    BlockNode block = block();
    return new ForDeclarationNode(variableDeclarationNode, expression, assignmentStatement, block);
  }

  private ReturnStatementNode returnStatement() throws IOException {
    match(RETURN);
    TokenType peek = currentToken.getType();
    if (peek == IDENTIFIER
        || peek == INTEGER
        || peek == TRUE
        || peek == FALSE
        || peek == STRING
        || peek == MINUS
        || peek == NOT
        || peek == LPAREN) {
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
        || currentToken.getType() == NEW
        || currentToken.getType() == LBRACKET) {

      if (isOperand(currentToken)) {
        if (currentToken.getType() == IDENTIFIER) {
          outputQueue.push(new IdentifierNode(currentToken));
        } else {
          outputQueue.push(new LiteralNode(currentToken));
        }
        currentToken = lexer.nextToken();
        // is it an array creation
      } else if (currentToken.getType() == NEW) {
        match(NEW);
        TypeNode type = type();
        if (type.isArray()) {
          List<ExpressionNode> expressions = new ArrayList<>();
          match(LBRACKET);
          expressions.add(expression());
          while (currentToken.getType() == COMMA) {
            match(COMMA);
            expressions.add(expression());
          }
          match(RBRACKET);
          outputQueue.push(new ArrayExpressionNode(type, expressions));
        } else {
          List<ObjectFieldNode> fields = new ArrayList<>();
          match(LBRACE);
          while (currentToken.getType() == IDENTIFIER) {
            IdentifierNode identifier = identifier();
            match(COLON);
            ExpressionNode expression = expression();
            fields.add(new ObjectFieldNode(identifier, expression));
          }
          match(RBRACE);
          outputQueue.push(new ObjectExpressionNode(type, fields));
        }
      } else if (isOperator(currentToken)) {
        while (!operatorStack.isEmpty() && hasPrecedence(currentToken, operatorStack.peek())) {
          Token operator = operatorStack.pop();
          ExpressionNode right = outputQueue.pop();
          ExpressionNode left = outputQueue.pop();
          outputQueue.push(new BinaryExpressionNode(left, new BinaryOperatorNode(operator), right));
        }
        operatorStack.push(currentToken);
        currentToken = lexer.nextToken();
      } else if (currentToken.getType() == LPAREN) {
        operatorStack.push(currentToken);
        currentToken = lexer.nextToken();
      } else if (currentToken.getType() == RPAREN) {
        while (operatorStack.peek().getType() != LPAREN) {
          Token operator = operatorStack.pop();
          ExpressionNode right = outputQueue.pop();
          ExpressionNode left = outputQueue.pop();
          outputQueue.push(new BinaryExpressionNode(left, new BinaryOperatorNode(operator), right));
        }
        Token token = operatorStack.pop(); // Discard the left parenthesis
        if (token.getType() != LPAREN) {
          throw new RuntimeException("Invalid token: " + token);
        }
        currentToken = lexer.nextToken();
      } else {
        throw new RuntimeException("Invalid token: " + currentToken);
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
    return EnumSet.of(INTEGER, STRING, IDENTIFIER, TRUE, FALSE).contains(token.getType());
  }

  private boolean isOperator(Token token) {
    return EnumSet.of(
            PLUS,
            MINUS,
            MULTIPLY,
            DIVIDE,
            AND,
            OR,
            MODULO,
            EQUAL_EQUAL,
            NOT_EQUAL,
            LESS,
            LESS_EQUAL,
            GREATER,
            GREATER_EQUAL)
        .contains(token.getType());
  }

  private boolean hasPrecedence(Token token1, Token token2) {
    if (token2.getType() == LPAREN || token2.getType() == RPAREN) {
      return false;
    }
    return (token1.getType() != MULTIPLY && token1.getType() != DIVIDE)
        || (token2.getType() != PLUS && token2.getType() != MINUS);
  }

  private IdentifierNode identifier() throws IOException {
    Token token = currentToken;
    match(IDENTIFIER);
    return new IdentifierNode(token);
  }

  private void match(TokenType expected) throws IOException {
    if (currentToken.getType() == expected) {
      currentToken = lexer.nextToken();
    } else {
      throw new IllegalStateException(
          "Unexpected token: "
              + currentToken.getType()
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
