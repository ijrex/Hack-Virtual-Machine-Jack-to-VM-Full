package compilationEngine.symboltable;

import java.util.LinkedHashMap;

import token.*;
import tokenlib.Keyword;

import compilationEngine.symboltable.util.*;

public class SymbolTable {

  LinkedHashMap<String, SymbolEntry> table;

  Keyword lastKind;
  int key;

  public SymbolTable() {
    table = new LinkedHashMap<String, SymbolEntry>();
    key = -1;
  }

  public void add(Token token) {

    Keyword kind = token.getKind();

    lastKind = (lastKind == null ? kind : lastKind);

    key = (Util.MatchValue(kind, lastKind)) ? key + 1 : 0;

    table.put(token.getValue(), new SymbolEntry(token, key));

    lastKind = kind;
  }

  public void print() {
    for (String key : table.keySet()) {
      String str = key + " -> " + table.get(key).print();

      System.out.println(str);
    }
  }
}
