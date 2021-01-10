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

  public String printValue() {
    return value;
  }

  public TokenType getType() {
    return type;
  }

  public String getLabel() {
    return null;
  }

  public Keyword getKeyword() {
    return null;
  }

  public Symbol getSymbol() {
    return null;
  }

  public String getFormattedType() {
    return this.type.toString().toLowerCase();
  }

  public String parse(String tabs) {
    return tabs + "<" + this.getLabel() + "> " + this.printValue() + " </" + this.getLabel() + ">\n";
  }

  // Identifier only

  public void setIdentifierCat(IdentifierCat str) {
  }

  public void setIdentifierCat(String str) {
  }

  public IdentifierCat getIdentifierCat() {
    return null;
  }

  public void setRunningIndex(int x) {
  }

  public int getRunningIndex() {
    return -1;
  }

  public void setVarType(String str) {
  }

  public String getVarType() {
    return null;
  }

  // intConst only

  public int getIntValue() {
    return -1;
  }
}
