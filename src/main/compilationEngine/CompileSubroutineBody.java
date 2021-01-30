package compilationEngine;

import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolKind;
import compilationEngine.vmwriter.VM;

public class CompileSubroutineBody extends Compile {

  Compile compileVarDec;
  Compile compileStatements;

  public CompileSubroutineBody() {
    routineLabel = "subroutineBody";
  }

  private String buildCommand() {
    String command = VM.writeFunction(functionName, scopedSymbolTable.getKindAmount(SymbolKind.VAR));


    if (functionType == Keyword.CONSTRUCTOR) {
      command += VM.writePush("constant", classSymbolTable.getKindAmount(SymbolKind.FIELD));
      command += VM.writeCall("Memory.alloc", 1);
      command += VM.writePop("pointer", 0);
    }

    if (functionType == Keyword.METHOD) {
      command += VM.writePush("argument", 0);
      command += VM.writePop("pointer", 0);
    }

    return command;
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        return passActive(passer.matchSymbol(activeToken, Symbol.BRACE_L));
      case 1:
        if (passer.matchKeyword(activeToken, Keyword.VAR) && compileVarDec == null)
          compileVarDec = new CompileVarDec();
        if (compileVarDec != null)
          return handleChildClass(compileVarDec);
        pos++;
      case 2:
        if (passer.matchKeyword(activeToken, Keyword.VAR)) {
          compileVarDec = null;
          pos--;
          return handleRoutine();
        }
        pos++;
        return buildCommand() + handleRoutine();
      case 3:
        if (compileStatements == null)
          compileStatements = new CompileStatements();
        return handleChildClass(compileStatements);
      case 4:
        return passActive(passer.matchSymbol(activeToken, Symbol.BRACE_R));
      case 5:
        return endRoutine();
      default:
        return fail();
    }
  }

}
