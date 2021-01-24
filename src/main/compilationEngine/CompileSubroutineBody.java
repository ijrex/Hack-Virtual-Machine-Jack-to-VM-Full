package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolKind;
import compilationEngine.util.*;
import compilationEngine.vmwriter.VM;

public class CompileSubroutineBody extends Compile {

  Compile compileVarDec;
  Compile compileStatements;

  public CompileSubroutineBody(int _tab) {
    super(_tab);
    wrapperLabel = "subroutineBody";
  }

  private String buildCommand() {
    String command = VM.writeFunction(functionName, numLocals);

    if (functionType == Keyword.CONSTRUCTOR) {
      command += VM.writePush("constant", numFieldVars);
      command += VM.writeCall("Memory.alloc", 1);
      command += VM.writePop("pointer", 0);
    }

    if (functionType == Keyword.METHOD) {
      command += VM.writePush("argument", 0);
      command += VM.writePop("pointer", 0);
    }

    return command;
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return passToken(token, Match.symbol(token, Symbol.BRACE_L));
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
        numLocals = scopedSymbolTable.getKindAmount(SymbolKind.VAR);
        return buildCommand() + handleToken(token);
      case 3:
        if (compileStatements == null)
          compileStatements = new CompileStatements(tab);
        return handleChildClass(compileStatements, token);
      case 4:
        return passToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 5:
        return postfix();
      default:
        return fail();
    }
  }

}
