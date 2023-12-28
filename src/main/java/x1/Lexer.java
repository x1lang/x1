package x1;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import x1.model.Token;
import x1.model.TokenType;

public class Lexer {
  private static final Map<String, TokenType> KEYWORDS;

  static {
    KEYWORDS = new HashMap<>();
    KEYWORDS.put("this", TokenType.THIS);
    KEYWORDS.put("type", TokenType.TYPE);
    KEYWORDS.put("function", TokenType.FUNCTION);
    KEYWORDS.put("var", TokenType.VAR);
    KEYWORDS.put("if", TokenType.IF);
    KEYWORDS.put("else", TokenType.ELSE);
    KEYWORDS.put("for", TokenType.FOR);
    KEYWORDS.put("foreach", TokenType.FOREACH);
    KEYWORDS.put("return", TokenType.RETURN);
    KEYWORDS.put("true", TokenType.TRUE);
    KEYWORDS.put("false", TokenType.FALSE);
  }

  private final InputStream in;
  private int currentChar;
  @Getter private int line = 1;
  @Getter private int column = 1;

  Lexer(InputStream in) throws IOException {
    this.in = in;
    this.currentChar = in.read();
  }

  Token nextToken() throws IOException {
    while (Character.isWhitespace(currentChar)) {
      consume();
    }

    if (currentChar == -1) {
      return new Token(TokenType.EOF, "");
    }

    if (Character.isLetter(currentChar)) {
      return identifier();
    }

    if (Character.isDigit(currentChar)) {
      return number();
    }

    switch (currentChar) {
      case '.':
        consume();
        return new Token(TokenType.DOT, ".");
      case ';':
        consume();
        return new Token(TokenType.SEMICOLON, ";");
      case ',':
        consume();
        return new Token(TokenType.COMMA, ",");
      case '{':
        consume();
        return new Token(TokenType.LBRACE, "{");
      case '}':
        consume();
        return new Token(TokenType.RBRACE, "}");
      case ':':
        consume();
        return new Token(TokenType.COLON, ":");
      case '+':
        consume();
        return new Token(TokenType.PLUS, "+");
      case '-':
        consume();
        return new Token(TokenType.MINUS, "-");
      case '*':
        consume();
        return new Token(TokenType.MULTIPLY, "*");
      case '/':
        consume();
        return new Token(TokenType.DIVIDE, "/");
      case '%':
        consume();
        return new Token(TokenType.MODULO, "%");
      case '[':
        consume();
        return new Token(TokenType.LBRACKET, "[");
      case ']':
        consume();
        return new Token(TokenType.RBRACKET, "]");
      case '(':
        consume();
        return new Token(TokenType.LPAREN, "(");
      case ')':
        consume();
        return new Token(TokenType.RPAREN, ")");
      case '=':
        consume();
        if (currentChar == '=') {
          consume();
          return new Token(TokenType.EQUAL_EQUAL, "==");
        } else {
          return new Token(TokenType.EQUAL, "=");
        }
      case '!':
        consume();
        if (currentChar == '=') {
          consume();
          return new Token(TokenType.NOT_EQUAL, "!=");
        } else {
          return new Token(TokenType.NOT, "!");
        }
      case '<':
        consume();
        if (currentChar == '=') {
          consume();
          return new Token(TokenType.LESS_EQUAL, "<=");
        } else {
          return new Token(TokenType.LESS, "<");
        }
      case '>':
        consume();
        if (currentChar == '=') {
          consume();
          return new Token(TokenType.GREATER_EQUAL, ">=");
        } else {
          return new Token(TokenType.GREATER, ">");
        }
      case '&':
        consume();
        if (currentChar == '&') {
          consume();
          return new Token(TokenType.AND, "&&");
        } else {
          throw new Error("Invalid character: " + (char) currentChar);
        }
      case '|':
        consume();
        if (currentChar == '|') {
          consume();
          return new Token(TokenType.OR, "||");
        } else {
          throw new IllegalStateException(
              "Invalid character: " + (char) currentChar + " at " + line + ":" + column);
        }
      case '"':
        return stringLiteral();
      default:
        throw new IllegalStateException(
            "Invalid character: " + (char) currentChar + " at " + line + ":" + column);
    }
  }

  private void consume() throws IOException {
    currentChar = in.read();
    column++;
    if (currentChar == '\n') {
      line++;
      column = 1;
    }
  }

  private Token identifier() throws IOException {
    StringBuilder buffer = new StringBuilder();
    while (Character.isLetterOrDigit(currentChar) || currentChar == '.') {
      buffer.append((char) currentChar);
      consume();
    }
    String identifier = buffer.toString();
    return new Token(KEYWORDS.getOrDefault(identifier, TokenType.IDENTIFIER), identifier);
  }

  private Token number() throws IOException {
    StringBuilder buffer = new StringBuilder();
    while (Character.isDigit(currentChar)) {
      buffer.append((char) currentChar);
      consume();
    }
    return new Token(TokenType.NUMBER, buffer.toString());
  }

  private Token stringLiteral() throws IOException {
    StringBuilder buffer = new StringBuilder();
    consume(); // consume the opening quote
    while (currentChar != '"') {
      buffer.append((char) currentChar);
      consume();
    }
    consume(); // consume the closing quote
    return new Token(TokenType.STRING, buffer.toString());
  }

  @Override
  public String toString() {
    return "[" + line + ":" + column + "]" + (char) currentChar;
  }
}
