package compilationenginexml.symboltable.util;

import compilationenginexml.symboltable.SymbolKind;

public class Util {
  public static SymbolKind toSymbolKind(String input) {
    for (SymbolKind kind : SymbolKind.values()) {
      if (kind.toString().equalsIgnoreCase(input)) {
        return kind;
      }
    }
    // @todo: throw error
    return SymbolKind.NULL;
  }
}
