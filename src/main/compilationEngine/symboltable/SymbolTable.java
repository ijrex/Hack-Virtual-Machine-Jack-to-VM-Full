package compilationEngine.symboltable;

import java.io.IOException;
import java.util.LinkedHashMap;

import token.*;

import compilationEngine.symboltable.util.*;
import errormessage.ErrorMessage;

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

  public SymbolEntry add(Token token, String _type, String _kind) throws IOException {

    String value = token.getValue();

    SymbolKind kind = Util.toSymbolKind(_kind);
    SymbolType type = symbolTypes.handleNew(_type);

    lastKind = (lastKind == null) ? kind : lastKind;

    key = (kind == lastKind) ? key + 1 : 0;

    table.put(value, new SymbolEntry(token, type, kind, key));

    lastKind = kind;

    return table.get(value);
  }

  public void incKey() {
    key++;
  }

  public SymbolEntry find(Token token) {
    String key = token.getValue();

    if (table.containsKey(key))
      return table.get(key);

    return null;
  }

  public int getKindAmount(SymbolKind kind) {
    int numKinds = 0;
    for (String key : table.keySet()) {
      if (table.get(key).getKind() == kind)
      numKinds++;
    }
    return numKinds;
  }

  // For debugging

  public String print() {
    String[] headers = new String[]{"Name", "Kind", "Type", "Running Index"};

    String data = "";

    for (String key : table.keySet()) {
      data += table.get(key).print() + ",";
    }
    return ErrorMessage.printTable(headers, data);
  }
}
