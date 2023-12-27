package x1.model;

import lombok.Value;

@Value
public class Token {
  TokenType type;
  String text;

  @Override
  public String toString() {
    return "(" + type + "," + text + ')';
  }
}
