package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.util.*;

public class CompileParameterList extends Compile {

  String varType;

  public CompileParameterList(int _tab) {
    super(_tab);
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
        if (Match.type(token)) {
          varType = token.getValue();
          return parseToken(token, true);
        }
      case 1:
        if (Match.identifier(token)) {
          SymbolEntry symbolEntry = scopedSymbolTable.add(token, varType, "ARGUMENT");
          return parseSymbolEntry(symbolEntry, true);
        }
        return fail();
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
