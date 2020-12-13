package compilationEngine.symboltable;

import java.util.LinkedHashMap;

import token.*;

import compilationEngine.symboltable.util.*;

public class SymbolTable {

  LinkedHashMap<String, SymbolEntry> table;

  SymbolTypes symbolTypes;

  SymbolKind lastKind;
  int key;

  public SymbolTable() {
    table = new LinkedHashMap<String, SymbolEntry>();

    symbolTypes = new SymbolTypes();

    key = -1;
  }

  public SymbolEntry add(Token token, String _type, String _kind) {

    String value = token.getValue();

    SymbolKind kind = Util.toSymbolKind(_kind);
    SymbolType type = symbolTypes.handleNew(_type);

    lastKind = (lastKind == null) ? kind : lastKind;

    key = (kind == lastKind) ? key + 1 : 0;

    table.put(value, new SymbolEntry(token, type, kind, key));

    lastKind = kind;

    return table.get(value);
  }

  public SymbolEntry find(Token token) {
    String key = token.getValue();

    if(table.containsKey(key))
      return table.get(key);

    return null;
  }

  public int getSize() {
    return table.size();
  }

  public void print() {
    for (String key : table.keySet()) {
      String str = key + " -> " + table.get(key).print();

      System.out.println(str);
    }
  }
}
