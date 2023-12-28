package x1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import x1.model.*;

class ParserTest {

  @Test
  void example() throws Exception {
    ClassLoader classLoader = ParserTest.class.getClassLoader();
    try (InputStream in = classLoader.getResourceAsStream("example.x1")) {
      Lexer lexer = new Lexer(Objects.requireNonNull(in));
      Parser parser = new Parser(lexer);
      CompilationUnitNode compilationUnitNode = parser.parse();

      CompilationUnitNode expected =
          new CompilationUnitNode(
              Collections.emptyList(),
              Collections.singletonList(
                  new MethodDeclarationNode(
                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "sum")),
                      new ParameterListNode(
                          Arrays.asList(
                              new ParameterNode(
                                  new IdentifierNode(new Token(TokenType.IDENTIFIER, "v")),
                                  new TypeNode(
                                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "Int")),
                                      false)),
                              new ParameterNode(
                                  new IdentifierNode(new Token(TokenType.IDENTIFIER, "n")),
                                  new TypeNode(
                                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "Int")),
                                      false)))),
                      new TypeNode(
                          new IdentifierNode(new Token(TokenType.IDENTIFIER, "Int")), false),
                      new BlockNode(
                          Arrays.asList(
                              new ForDeclarationNode(
                                  new VariableDeclarationNode(
                                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "i")),
                                      new TypeNode(
                                          new IdentifierNode(
                                              new Token(TokenType.IDENTIFIER, "Int")),
                                          false),
                                      new LiteralNode(new Token(TokenType.NUMBER, "0"))),
                                  new BinaryExpressionNode(
                                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "i")),
                                      new BinaryOperatorNode(new Token(TokenType.LESS, "<")),
                                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "n"))),
                                  new AssignmentStatementNode(
                                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "i")),
                                      new BinaryExpressionNode(
                                          new IdentifierNode(new Token(TokenType.IDENTIFIER, "i")),
                                          new BinaryOperatorNode(new Token(TokenType.PLUS, "+")),
                                          new LiteralNode(new Token(TokenType.NUMBER, "1")))),
                                  new BlockNode(
                                      Collections.singletonList(
                                          new IfStatementNode(
                                              new BinaryExpressionNode(
                                                  new BinaryExpressionNode(
                                                      new IdentifierNode(
                                                          new Token(TokenType.IDENTIFIER, "i")),
                                                      new BinaryOperatorNode(
                                                          new Token(TokenType.MODULO, "%")),
                                                      new LiteralNode(
                                                          new Token(TokenType.NUMBER, "2"))),
                                                  new BinaryOperatorNode(
                                                      new Token(TokenType.EQUAL_EQUAL, "==")),
                                                  new LiteralNode(
                                                      new Token(TokenType.NUMBER, "0"))),
                                              new BlockNode(
                                                  Collections.singletonList(
                                                      new AssignmentStatementNode(
                                                          new IdentifierNode(
                                                              new Token(TokenType.IDENTIFIER, "v")),
                                                          new BinaryExpressionNode(
                                                              new IdentifierNode(
                                                                  new Token(
                                                                      TokenType.IDENTIFIER, "v")),
                                                              new BinaryOperatorNode(
                                                                  new Token(TokenType.PLUS, "+")),
                                                              new IdentifierNode(
                                                                  new Token(
                                                                      TokenType.IDENTIFIER,
                                                                      "i")))))),
                                              null)))),
                              new VariableDeclarationNode(
                                  new IdentifierNode(new Token(TokenType.IDENTIFIER, "y")),
                                  new TypeNode(
                                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "Int")),
                                      true),
                                  new ArrayExpressionNode(
                                      new TypeNode(
                                          new IdentifierNode(
                                              new Token(TokenType.IDENTIFIER, "Int")),
                                          true),
                                      Arrays.asList(
                                          new LiteralNode(new Token(TokenType.NUMBER, "1")),
                                          new LiteralNode(new Token(TokenType.NUMBER, "2"))))),
                              new ForEachDeclarationNode(
                                  new IdentifierNode(new Token(TokenType.IDENTIFIER, "x")),
                                  new TypeNode(
                                      new IdentifierNode(new Token(TokenType.IDENTIFIER, "Int")),
                                      false),
                                  new IdentifierNode(new Token(TokenType.IDENTIFIER, "y")),
                                  new BlockNode(Collections.emptyList())),
                              new ReturnStatementNode(
                                  new IdentifierNode(new Token(TokenType.IDENTIFIER, "v"))))))));
      assertEquals(expected, compilationUnitNode);
    }
  }
}
