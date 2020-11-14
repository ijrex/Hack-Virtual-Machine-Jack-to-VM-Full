package tokenlib.util;

import tokenlib.*;

public class Util {
  public static String getKeywordReg() {
    String reg = "^(";
    for (Keyword keyword : Keyword.values()) {
      reg += keyword.getValue() + "|";
    }
    return reg.substring(0, reg.length() - 1) + ")\\s+";
  }

  public static String getSymbolReg() {
    String reg = "^(";
    for (Symbol symbol : Symbol.values()) {
      reg += "\\" + symbol.getValue() + "|";
    }
    return reg.substring(0, reg.length() - 1) + ")";
  }
}
