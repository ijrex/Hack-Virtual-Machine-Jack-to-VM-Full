package compilationEngine.symboltable;

import token.*;

public class SymbolEntry {

  String name;
  String type;
  SymbolKind kind;
  int key;

  public SymbolEntry(Token token, SymbolKind kind, int key) {
    this.name = token.getValue();
    this.type = "TODO";
    this.key = key;
    this.kind = kind;
  }

  public String print() {
    return this.name + ", " + this.type + ", " + this.kind + ", " + this.key;
  }
}
