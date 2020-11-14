package token;

import tokenlib.*;

public class StrConstToken extends Token {

  public StrConstToken(String value) {
    super(value);
    this.type = TokenType.STRING_CONST;
  }

  public String getLabel() {
    return "stringConstant";
  }

  public String printValue() {
    return value.substring(1, value.length() - 1);
  }
}
