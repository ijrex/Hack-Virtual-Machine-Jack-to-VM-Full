package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileSubroutineBody extends Compile {

  Compile compileVarDec;
  Compile compileStatements;

  SymbolTable scopedSymbolTable;

  public CompileSubroutineBody(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable);
    wrapperLabel = "subroutineBody";

    scopedSymbolTable = _scopedSymbolTable;
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 1:
        if (Match.keyword(token, Keyword.VAR) && compileVarDec == null)
          compileVarDec = new CompileVarDec(tab, classSymbolTable, scopedSymbolTable);
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
      case 3:
        if (compileStatements == null)
          compileStatements = new CompileStatements(tab, classSymbolTable);
        return handleChildClass(compileStatements, token);
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 5:
        scopedSymbolTable.print();
        return postfix();
      default:
        return fail();
    }
  }

}
