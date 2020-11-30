package compilationEngine.symboltable;

import token.*;

public class SymbolEntry {

  String name;
  SymbolType type;
  SymbolKind kind;
  int key;

  public SymbolEntry(Token token, SymbolType type, SymbolKind kind, int key) {
    this.name = token.getValue();
    this.type = type;
    this.key = key;
    this.kind = kind;
  }

  public String print() {
    return this.name + ", " + this.type.getValue() + ", " + this.kind + ", " + this.key;
  }
}
