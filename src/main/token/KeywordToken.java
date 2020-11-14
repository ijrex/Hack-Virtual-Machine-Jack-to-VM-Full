package token;

import tokenlib.*;

public class KeywordToken extends Token {

  Keyword keyword;

  public KeywordToken(String value, Keyword _keyword) {
    super(value);
    this.type = TokenType.KEYWORD;
    this.keyword = _keyword;
  }

  public Keyword getKeyword() {
    return keyword;
  }

  public String getLabel() {
    return "keyword";
  }
}
