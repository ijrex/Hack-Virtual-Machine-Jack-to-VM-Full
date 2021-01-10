package token;

import tokenlib.TokenType;

public class IdentifierToken extends Token {

  IdentifierCat identifierCat = IdentifierCat.NONE;
  String varType;
  int runningIndex = -1;

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
    str += tabs + "\t" + "<name> " + this.printValue() + " </name>\n";
    str += tabs + "\t" + "<category> " + this.identifierCat + " </category>\n";
    str += tabs + "\t" + "<runningIndex> " + this.runningIndex + " </runningIndex>\n";
    str += tabs + "</" + this.getLabel() + ">\n";

    return str;
  }

  public void setIdentifierCat(IdentifierCat t) {
    this.identifierCat = t;
  }

  public void setIdentifierCat(String t) {
    for (IdentifierCat i : IdentifierCat.values()) {
      if (t.equalsIgnoreCase(i.toString())) {
        this.identifierCat = i;
      }
    }
  }

  public IdentifierCat getIdentifierCat() {
    return this.identifierCat;
  }

  public void setVarType(String t) {
    this.varType = t;
  }

  public String getVarType() {
    return this.varType;
  }

  public void setRunningIndex(int x) {
    this.runningIndex = x;
  }

  public int getRunningIndex() {
    return runningIndex;
  }
}
