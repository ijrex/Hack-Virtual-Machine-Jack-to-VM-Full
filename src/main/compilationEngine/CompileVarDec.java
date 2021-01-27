package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.util.Match;

public class CompileVarDec extends Compile {
  String varType;

  public CompileVarDec() {
    super();
    wrapperLabel = "varDec";
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case -1:
        return prefix();
      case 0:
        return passToken(passer.matchKeyword(activeToken, Keyword.VAR));
      case 1:
        if (passer.isType(activeToken)) {
          if(passer.isIdentifier(activeToken))
            activeToken.setIdentifierCat(IdentifierCat.SYMBOL_DEC);
          varType = activeToken.getValue();
          return passToken();
        }
        return fail();
      case 2:
        if (passer.isIdentifier(activeToken)) {
          SymbolEntry symbolEntry = scopedSymbolTable.add(activeToken, varType, "VAR");
          return passSymbolEntry(symbolEntry, true);
        }
        return fail();
      case 3:
        if (passer.matchSymbol(activeToken, Symbol.COMMA))
          return passToken(2);
        if (passer.matchSymbol(activeToken, Symbol.SEMI_COLON))
          return passToken();
        return fail();
      case 4:
        return postfix();
      default:
        return fail();
    }
  }

}
