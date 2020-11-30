package token;

import tokenlib.TokenType;

public class IdentifierToken extends Token {

  public enum IdentifierType {
    CLASS, SUBROUTINE, NONE,
  }

  public enum IdentifierKind {
    VAR, ARGUMENT, STATIC, FIELD,
  }

  IdentifierType identifierType;
  IdentifierKind identifierKind;
  int runningIndex;

  public IdentifierToken(String value) {
    super(value);
    this.type = TokenType.IDENTIFIER;
  }

  public String getLabel() {
    return "identifier";
  }

  // Identifier only

  public void setIdentifierType(String str) {
    for (IdentifierType t : IdentifierType.values()) {
      if (str.equalsIgnoreCase(t.name())) {
        this.identifierType = t;
      }
    }
  }

  public IdentifierType getIdentifierType() {
    return this.identifierType;
  }

  public void setIdentifierKind(String str) {
    for (IdentifierKind k : IdentifierKind.values()) {
      if (str.equalsIgnoreCase(k.name())) {
        this.identifierKind = k;
      }
    }
  }

  public IdentifierKind getIdentifierKind() {
    return this.identifierKind;
  }

  public void setRunningIndex(int x) {
    this.runningIndex = x;
  }

  public int getRunningIndex() {
    return runningIndex;
  }
}
