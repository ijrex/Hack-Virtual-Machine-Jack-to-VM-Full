package token;

import tokenlib.TokenType;

public class IdentifierToken extends Token {

  String varType;
  int runningIndex = -1;

  public IdentifierToken(String value) {
    super(value);
    this.type = TokenType.IDENTIFIER;
  }
}
