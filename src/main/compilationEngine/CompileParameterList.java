package compilationEngine;

import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.util.*;

public class CompileParameterList extends Compile {

  String varType;
  Boolean incKeyForMethod = false;

  public CompileParameterList() {
    wrapperLabel = "parameterList";
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        if (passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R))
          return postfix();
        pos++;
      case 1:
        if (passer.isType(activeToken)) {
          varType = activeToken.getValue();

          if(!incKeyForMethod && functionType == Keyword.METHOD) {
            incKeyForMethod = true;
            scopedSymbolTable.incKey();
          }
          return passActive();
        }
      case 2:
        if (Match.identifier(activeToken)) {
          SymbolEntry symbolEntry = scopedSymbolTable.add(activeToken, varType, "ARGUMENT");
          return passSymbolEntry(symbolEntry, true);
        }
        return fail();
      case 3:
        if (Match.symbol(activeToken, Symbol.COMMA))
          return passActive( 0);
        if (Match.symbol(activeToken, Symbol.PARENTHESIS_R))
          return postfix();
      default:
        return fail();
    }
  }

}
