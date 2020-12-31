package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;
import compilationEngine.vmwriter.VM;

public class CompileSubroutineBody extends Compile {

  Compile compileVarDec;
  Compile compileStatements;

  public CompileSubroutineBody(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "subroutineBody";
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
          compileStatements = new CompileStatements(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileStatements, token);
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 5:
        return VM.writeFunction(functionName, -1) + postfix();
      default:
        return fail();
    }
  }

}
