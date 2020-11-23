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

  public void setKind(Keyword kind) {
    // @todo: Limit varKind enums
    // STATIC, FIELD or (argument) VAR

    this.kind = kind;
  }

  public Keyword getKind() {
    return this.kind;
  }

  public void setIdentifierType(String identifierType) {
    // @todo: Limit varKind enums
    // IDENTIFIER, CHAR, INT, BOOLEAN

    this.identifierType = identifierType;
  }

  public String getIdentifierType() {
    return this.identifierType;
  }
}
