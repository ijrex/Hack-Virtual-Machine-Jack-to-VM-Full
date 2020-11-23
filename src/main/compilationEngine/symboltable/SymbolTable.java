package compilationEngine.symboltable;

import java.util.LinkedHashMap;

import token.*;
import tokenlib.Keyword;

import compilationEngine.symboltable.util.*;

public class SymbolTable {

  LinkedHashMap<String, SymbolEntry> table;

  SymbolKind lastKind;
  int key;

  public SymbolTable() {
    table = new LinkedHashMap<String, SymbolEntry>();
    key = -1;
  }

  public void add(Token token, String type, String _kind) {

    SymbolKind kind = Util.toSymbolKind(_kind);

    // @todo: SymbolType to symbolType hashmap
    // INT, CHAR, BOOLEAN, IDENTIFIER
    // SymbolType type = Util.toSymbolType(_type);

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
