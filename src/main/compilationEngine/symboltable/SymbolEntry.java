package compilationEngine.symboltable;

import token.*;
import tokenlib.Keyword;
import tokenlib.*;

public class SymbolEntry {

  String name;
  TokenType type;
  Keyword kind;
  int key;

  public SymbolEntry(Token token, Keyword _kind, int key) {
    this.name = token.getValue();
    this.type = token.getType();
    this.kind = _kind;
    this.key = key;
  }

  public String print() {
    return this.name + ", " + this.type + ", " + this.kind + ", " + this.key;
  }
}
