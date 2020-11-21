package compilationEngine.symboltable.util;

import tokenlib.Keyword;

public class Util {
  public static boolean MatchValue(Keyword arg1, Keyword arg2) {
    return arg1.getValue().matches(arg2.getValue());
  };
}
