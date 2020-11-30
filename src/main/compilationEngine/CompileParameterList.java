package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileParameterList extends Compile {

  public CompileParameterList(int _tab, SymbolTable _classSymbolTable) {
    super(_tab, _classSymbolTable);
    wrapperLabel = "parameterList";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -2:
        return postfix();
      case -1:
        if (Match.symbol(token, Symbol.PARENTHESIS_R))
          return prefix(token, -2);
        return prefix(token);
      case 0:
        return parseToken(token, Match.type(token));
      case 1:
        return parseToken(token, Match.identifier(token));
      case 2:
        if (Match.symbol(token, Symbol.COMMA))
          return parseToken(token, true, 0);
        if (Match.symbol(token, Symbol.PARENTHESIS_R))
          return postfix();
      default:
        return fail();
    }
  }

}
