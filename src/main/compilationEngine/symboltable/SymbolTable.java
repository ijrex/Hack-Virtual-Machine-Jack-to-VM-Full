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

  public void add(Token token, String _type, String _kind) {

    SymbolKind kind = Util.toSymbolKind(_kind);

    SymbolType type = symbolTypes.handleNew(_type);

    lastKind = (lastKind == null) ? kind : lastKind;

    key = (kind == lastKind) ? key + 1 : 0;

    table.put(token.getValue(), new SymbolEntry(token, type, kind, key));

    lastKind = kind;
  }

  public void print() {
    for (String key : table.keySet()) {
      String str = key + " -> " + table.get(key).print();

      System.out.println(str);
    }
  }
}
