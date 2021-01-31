package compilationEngine;

import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

public class CompileParameterList extends Compile {

  String varType;
  Boolean incKeyForMethod = false;

  public CompileParameterList() {
    routineLabel = "parameterList";
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        if (passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R))
          return endRoutine();
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
        if (passer.isIdentifier(activeToken)) {
          scopedSymbolTable.add(activeToken, varType, "ARGUMENT");
          return passActive();
        }
        return fail();
      case 3:
        if (passer.matchSymbol(activeToken, Symbol.COMMA))
          return passActive(0);
        if (passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R))
          return endRoutine();
      default:
        return fail();
    }
  }

}
