package token;

import tokenlib.TokenType;

public class IdentifierToken extends Token {

  IdentifierCat identifierCat = IdentifierCat.NONE;
  IdentifierKind identifierKind = IdentifierKind.NONE;
  int runningIndex = -1;
  boolean isBeingDefined;

  public IdentifierToken(String value) {
    super(value);
    this.type = TokenType.IDENTIFIER;
  }

  public String getLabel() {
    return "identifier";
  }

  // Identifier only

  public String parse(String tabs) {
    String str = "";

    str += tabs + "<" + this.getLabel() + ">\n";
    str += tabs + "\t" + "<name> " +  this.printValue() + " </name>\n";
    str += tabs + "\t" + "<isBeingDefined> " +  this.isBeingDefined + " </isBeingDefined>\n";
    str += tabs + "\t" + "<category> " +  this.identifierCat + " </category>\n";
    str += tabs + "\t" + "<kind> " +  this.identifierKind + " </kind>\n";
    str += tabs + "\t" + "<runningIndex> " +  this.runningIndex + " </runningIndex>\n";
    str += tabs + "</" + this.getLabel() + ">\n";

    return str;
  }

  public void setIdentifierCat(IdentifierCat t) {
    this.identifierCat = t;
  }

  public IdentifierCat getIdentifierType() {
    return this.identifierCat;
  }

  public void setIdentifierKind(IdentifierKind k) {
    this.identifierKind = k;
  }

  public IdentifierKind getIdentifierKind() {
    return this.identifierKind;
  }

  public void setIdentifierBeingDefined(boolean b) {
    this.isBeingDefined = b;
  }

  public boolean getIdentifierBeingDefined() {
    return this.isBeingDefined;
  }

  public void setRunningIndex(int x) {
    this.runningIndex = x;
  }

  public int getRunningIndex() {
    return runningIndex;
  }
}
