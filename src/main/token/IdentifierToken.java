package token;

import tokenlib.Keyword;
import tokenlib.TokenType;

public class IdentifierToken extends Token {

  IdentifierKind kind;
  String identifierType;

  public IdentifierToken(String value) {
    super(value);
    this.type = TokenType.IDENTIFIER;
  }

  public String getLabel() {
    return "identifier";
  }

  public void setKind(String kind) {
    for (IdentifierKind k : IdentifierKind.values()) {
      if (kind.equalsIgnoreCase(k.toString())) {
        this.kind = k;
        return;
      }
    }
    // @todo: throw error
  }

  public IdentifierKind getKind() {
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
