package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;

public class CompileClass extends Compile {

  Compile compileClassVarDec;
  Compile compileSubroutineDec;

  public CompileClass() {
    wrapperLabel = "class";

    classSymbolTable = new SymbolTable();
  }

  public void setClassName(String _className) {
    className = _className;
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        return passActive(passer.matchKeyword(activeToken, Keyword.CLASS));
      case 1:
        if (passer.isIdentifier(activeToken)) {
          activeToken.setIdentifierCat(IdentifierCat.CLASS_DEC);
          return passActive();
        }
        return fail();
      case 2:
        return passActive(passer.matchSymbol(activeToken, Symbol.BRACE_L));
      case 3:
        if (passer.isClassVarDec(activeToken) && compileClassVarDec == null)
          compileClassVarDec = new CompileClassVarDec();
        if (compileClassVarDec != null)
          return handleChildClass(compileClassVarDec);
        pos++;
      case 4:
        if (passer.isClassVarDec(activeToken) && compileClassVarDec != null) {
          compileClassVarDec = null;
          pos--;
          return handleRoutine();
        }
        pos++;
      case 5:
        if (passer.isSubroutineDec(activeToken) && compileSubroutineDec == null)
          compileSubroutineDec = new CompileSubroutineDec();
        if (compileSubroutineDec != null)
          return handleChildClass(compileSubroutineDec);
        pos++;
      case 6:
        if (passer.isSubroutineDec(activeToken) && compileSubroutineDec != null) {
          compileSubroutineDec = null;
          pos--;
          return handleRoutine();
        }
        pos++;
      case 7:
        return passActive(passer.matchSymbol(activeToken, Symbol.BRACE_R)) + endRoutine();
      default:
        return fail();
    }
  }
}
