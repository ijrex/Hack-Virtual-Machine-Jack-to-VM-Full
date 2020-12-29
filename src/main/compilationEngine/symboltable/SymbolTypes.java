package compilationEngine.symboltable;

import java.util.HashMap;

public class SymbolTypes {

  HashMap<String, SymbolType> types;

  public SymbolTypes() {

    types = new HashMap<String, SymbolType>();

    types.put("int", new SymbolType("int"));
    types.put("char", new SymbolType("char"));
    types.put("boolean", new SymbolType("boolean"));
  }

  public SymbolType handleNew(String val) {
    if (types.get(val) == null) {
      types.put(val, new SymbolType(val));
    }
    return types.get(val);
  }

  public boolean isPrimitive(SymbolType type) {
    String value = type.getValue();

    switch (value) {
      case "int":
      case "char":
      case "boolean":
        return true;
      default:
        return false;
    }
  }
}
