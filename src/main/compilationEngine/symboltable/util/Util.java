package compilationEngine.symboltable.util;

import java.io.IOException;

import compilationEngine.symboltable.SymbolKind;
import errormessage.ErrorMessage;

public class Util {
  public static SymbolKind toSymbolKind(String input) throws IOException {
    for (SymbolKind kind : SymbolKind.values()) {
      if (kind.toString().equalsIgnoreCase(input)) {
        return kind;
      }
    }
    throw new IOException(ErrorMessage.header("CLASS SYMBOL TABLE") + ErrorMessage.info("Message", input + " could not be resolved to a symbol"));
  }
}
