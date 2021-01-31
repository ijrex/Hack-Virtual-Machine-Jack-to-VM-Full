package token;

import tokenlib.*;

public abstract class Token {
  protected String value;
  protected TokenType type;

  public Token(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public TokenType getType() {
    return type;
  }

  public Keyword getKeyword() {
    return null;
  }

  public Symbol getSymbol() {
    return null;
  }

  // intConst only

  public int getIntValue() {
    return -1;
  }
}
