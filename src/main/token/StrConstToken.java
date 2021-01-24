package token;

import tokenlib.*;

public class StrConstToken extends Token {

  public StrConstToken(String value) {
    super(value);
    this.type = TokenType.STRING_CONST;
  }
}
