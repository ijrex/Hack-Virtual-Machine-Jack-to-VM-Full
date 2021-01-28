package compilationEngine;

import token.*;
import tokenlib.*;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;

public class CompileSubroutineDec extends Compile {

  Compile compileParameterList;
  Compile compileSubroutineBody;

  public CompileSubroutineDec() {
    super();
    wrapperLabel = "subroutineDec";

    scopedSymbolTable = new SymbolTable();
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case -1:
        return prefix();
      case 0:
        if (passer.matchKeyword(activeToken, new Keyword[] { Keyword.CONSTRUCTOR, Keyword.FUNCTION, Keyword.METHOD })) {
          functionType = activeToken.getKeyword();
          return passToken();
        }
        return fail();
      case 1:
        if (passer.isReturnTypeType(activeToken)) {
          if (passer.isIdentifier(activeToken))
            activeToken.setIdentifierCat(IdentifierCat.CLASS);
          returnType = activeToken;
          return passToken();
        }
        return fail();
      case 2:
        if (passer.isIdentifier(activeToken)) {
          functionName = className + "." + activeToken.getValue();
          activeToken.setIdentifierCat(IdentifierCat.SUBROUTINE_DEC);
          return passToken();
        }
        return fail();
      case 3:
        return passToken(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_L));
      case 4:
        if (compileParameterList == null)
          compileParameterList = new CompileParameterList();
        return handleChildClass(compileParameterList);
      case 5:
        return passToken(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R));
      case 6:
        if (compileSubroutineBody == null)
          compileSubroutineBody = new CompileSubroutineBody();
        return handleChildClass(compileSubroutineBody);
      case 7:
        resetStaticStatements();
        return postfix();
      default:
        return fail();
    }
  }
}
