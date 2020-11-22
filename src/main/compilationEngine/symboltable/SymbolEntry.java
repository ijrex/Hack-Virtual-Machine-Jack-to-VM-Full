package compilationEngine.symboltable;

import token.*;

public class SymbolEntry {

  String name;
  String type;
  int key;
  IdentifierKind kind;

  public SymbolEntry(Token token, int key) {
    this.name = token.getValue();
    this.type = token.getIdentifierType();
    this.kind = token.getKind();
    this.key = key;
  }

  public String print() {
    return this.name + ", " + this.type + ", " + this.kind + ", " + this.key;
  }
}
