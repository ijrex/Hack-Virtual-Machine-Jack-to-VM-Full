package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.*;

public class CompileSubroutineBody extends Compile {

  Compile compileVarDec;
  Compile compileStatements;

  Token returnType;

  public CompileSubroutineBody(int _tab, Token _returnType) {
    super(_tab);
    wrapperLabel = "subroutineBody";
    returnType = _returnType;
  }

  private String buildCommand(int args) {
    return " " + args + "\n";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 1:
        if (Match.keyword(token, Keyword.VAR) && compileVarDec == null)
          compileVarDec = new CompileVarDec(tab);
        if (compileVarDec != null)
          return handleChildClass(compileVarDec, token);
        pos++;
      case 2:
        if (Match.keyword(token, Keyword.VAR)) {
          compileVarDec = null;
          pos--;
          return handleToken(token);
        }
        pos++;
        return buildCommand(scopedSymbolTable.getLocalsAmmount()) + handleToken(token);
      case 3:
        if (compileStatements == null)
          compileStatements = new CompileStatements(tab, returnType);
        return handleChildClass(compileStatements, token);
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 5:
        return postfix();
      default:
        return fail();
    }
  }

}
