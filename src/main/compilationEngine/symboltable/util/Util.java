package compilationEngine.symboltable.util;

import token.*;

public class Util {
  public static boolean MatchValue(IdentifierKind arg1, IdentifierKind arg2) {
    return arg1.toString().matches(arg2.toString());
  };
}
