package compilationEngine.symboltable;

import token.*;

public class SymbolEntry {

  String name;
  SymbolType type;
  SymbolKind kind;
  int key;
  boolean isPrimitveType;

  public SymbolEntry(Token token, SymbolType type, SymbolKind kind, int key, boolean isPrimitveType) {
    this.name = token.getValue();
    this.type = type;
    this.key = key;
    this.kind = kind;
    this.isPrimitveType = isPrimitveType;
  }

  public String print() {
    return this.name + ", " + this.type.getValue() + ", " + this.kind + ", " + this.key;
  }

  public String getName() {
    return this.name;
  }

  public int getKey() {
    return this.key;
  }

  public SymbolKind getKind() {
    return this.kind;
  }

  public String getKindToString() {
    return this.kind.toString();
  }

  public SymbolType getType() {
    return this.type;
  }

  public String getTypeToString() {
    return this.type.getValue();
  }

  public boolean getIsPrimitive() {
    return this.isPrimitveType;
  }

  private String parseValue(String str, String label, String tabs) {
    return tabs + "\t" + "<" + label + "> " + str + " </" + label + ">\n";
  }

  public String parse(String tabs) {
    String str = "";

    str += tabs + "<symbolEntry>\n";
    str += parseValue(this.name, "name", tabs);
    str += parseValue(this.type.getValue(), "type", tabs);
    str += parseValue(String.valueOf(this.key), "key", tabs);
    str += parseValue(this.kind.name(), "kind", tabs);
    str += parseValue(String.valueOf(isPrimitveType), "isPrimitive", tabs);
    str += tabs + "</symbolEntry>\n";

    return str;
  }
}
