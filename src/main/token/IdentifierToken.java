package token;

import tokenlib.Keyword;
import tokenlib.TokenType;

public class IdentifierToken extends Token {

  Keyword kind;
  String identifierType;

  public IdentifierToken(String value) {
    super(value);
    this.type = TokenType.IDENTIFIER;
  }

  public String getLabel() {
    return "identifier";
  }
}
