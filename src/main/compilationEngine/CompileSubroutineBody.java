package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolKind;
import compilationEngine.util.*;

public class CompileSubroutineBody extends Compile {

  Compile compileVarDec;
  Compile compileStatements;

  Keyword subroutineType;

  public CompileSubroutineBody(int _tab, Keyword subroutineType) {
    super(_tab);
    this.subroutineType = subroutineType;
    wrapperLabel = "subroutineBody";
  }

  private String buildCommand(int args) {
    String command = " " + args + "\n";

    if (subroutineType == Keyword.CONSTRUCTOR) {
      command += "push constant " + classSymbolTable.getKindAmount(SymbolKind.FIELD) + "\n";
      command += "call Memory.alloc 1\n";
      command += "pop pointer 0\n";
    }

    return command;
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
        return buildCommand(scopedSymbolTable.getKindAmount(SymbolKind.VAR)) + handleToken(token);
      case 3:
        if (compileStatements == null)
          compileStatements = new CompileStatements(tab);
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
