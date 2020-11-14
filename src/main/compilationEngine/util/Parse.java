package compilationEngine.util;

import token.Token;

public class Parse {
  public static String token(Token token) {
    String label = token.getLabel();
    return "<" + label + "> " + token.printValue() + " </" + label + ">\n";
  }
}
