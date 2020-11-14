package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.Match;

public class CompileClassVarDec extends Compile {

  public CompileClassVarDec(int _tab) {
    super(_tab);
    wrapperLabel = "classVarDec";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (Match.isClassVarDec(token))
          return parseToken(token, true);
        return fail();
      case 1:
        return parseToken(token, Match.type(token));
      case 2:
        return parseToken(token, Match.identifier(token));
      case 3:
        if (Match.symbol(token, Symbol.COMMA))
          return parseToken(token, true, 2);
        if (Match.symbol(token, Symbol.SEMI_COLON))
          return parseToken(token, true, 4);
        return fail();
      case 4:
        return postfix();
      default:
        return fail();
    }
  }

}
