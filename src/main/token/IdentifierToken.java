package token;

import tokenlib.*;

public class IdentifierToken extends Token {

  public IdentifierToken(String value) {
    super(value);
    this.type = TokenType.IDENTIFIER;
  }

  public String getLabel() {
    return "identifier";
  }
}
