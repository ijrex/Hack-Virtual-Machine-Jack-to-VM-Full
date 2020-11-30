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

  public String getName() {
    return this.name;
  }

  private String parseValue(String str, String label, String tabs) {
    return tabs + "\t" + "<" + label + "> " + str + " </" + label + ">\n";
  }

  public String parse(String tabs) {
    String str = "";

    str += tabs + "<identifier>\n";
    str += parseValue(this.name, "name", tabs);
    str += parseValue(this.type.getValue(), "type", tabs);
    str += parseValue(String.valueOf(this.key), "key", tabs);
    str += parseValue(this.kind.name(), "kind", tabs);
    str += tabs + "</identifier>\n";

    return str;
  }
}
