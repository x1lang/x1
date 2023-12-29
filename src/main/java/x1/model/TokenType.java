package x1.model;

public enum TokenType {
  EOF,
  IDENTIFIER,
  // literals
  NUMBER,
  STRING,
  TRUE,
  FALSE,
  // keywords
  TYPE,
  FUNCTION,
  VAR,
  IF,
  ELSE,
  FOR,
  FOREACH,
  RETURN,
  // operators
  NOT,
  PLUS,
  MINUS,
  MULTIPLY,
  DIVIDE,
  NOT_EQUAL,
  LESS_EQUAL,
  LESS,
  GREATER_EQUAL,
  GREATER,
  AND,
  OR,
  MODULO,
  EQUAL_EQUAL,
  // assignment
  EQUAL,
  // delimiters
  COLON,
  COMMA,
  SEMICOLON,
  // braces, brackets and parenthesis
  LBRACE,
  RBRACE,
  LBRACKET,
  RBRACKET,
  LPAREN,
  RPAREN,
  THIS,
  DOT,
  NEW,
}
