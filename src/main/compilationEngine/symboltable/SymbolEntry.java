package compilationEngine.symboltable;

import token.*;
import tokenlib.*;

public class SymbolEntry {

  String name;
  String type;
  int key;
  Keyword kind;

  public SymbolEntry(Token token, int key) {
    this.name = token.getValue();
    this.type = token.getIdentifierType();
    this.key = key;
    this.kind = token.getKind();
  }

  public String print() {
    return this.name + ", " + this.type + ", " + this.kind + ", " + this.key;
  }
}
