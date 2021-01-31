package compilationEngine;

import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.*;

public class CompileClassVarDec extends Compile {

  String varKind;
  String varType;

  public CompileClassVarDec() {
    routineLabel = "classVarDec";
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        if (passer.isClassVarDec(activeToken)){
          varKind = activeToken.getKeyword().toString();
          return passActive();
        }
        return fail();
      case 1:
        if (passer.isType(activeToken)) {
          varType = activeToken.getValue();
          return passActive();
        }
        return fail();
      case 2:
        if (passer.isIdentifier(activeToken)){
          // @todo: Check for duplicate and throw error
          classSymbolTable.add(activeToken, varType, varKind);
          return passActive();
        }
        return fail();
      case 3:
        if (passer.matchSymbol(activeToken, Symbol.COMMA))
          return passActive(2);
        if (passer.matchSymbol(activeToken, Symbol.SEMI_COLON))
          return passActive(4);
        return fail();
      case 4:
        return endRoutine();
      default:
        return fail();
    }
  }

}
