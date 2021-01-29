package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;

public class CompileVarDec extends Compile {
  String varType;

  public CompileVarDec() {
    super();
    routineLabel = "varDec";
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        return passActive(passer.matchKeyword(activeToken, Keyword.VAR));
      case 1:
        if (passer.isType(activeToken)) {
          varType = activeToken.getValue();
          return passActive();
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
          return passActive(2);
        if (passer.matchSymbol(activeToken, Symbol.SEMI_COLON))
          return passActive();
        return fail();
      case 4:
        return endRoutine();
      default:
        return fail();
    }
  }

}
