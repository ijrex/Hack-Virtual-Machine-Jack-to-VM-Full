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

  public String getName() {
    return this.name;
  }

  public int getKey() {
    return this.key;
  }

  public String getType() {
    return this.type.getValue();
  }

  public SymbolKind getKind() {
    return this.kind;
  }

  // For debugging

  public String print() {
    return this.name + ", " + this.type.getValue() + ", " + this.kind + ", " + this.key;
  }
}
