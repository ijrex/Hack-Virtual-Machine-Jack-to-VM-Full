package compilationEngine;

import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.vmwriter.VM;

public class CompileStatementReturn extends Compile {

  Compile compileExpression;

  public CompileStatementReturn() {
    routineLabel = "returnStatement";
  }

  private String buildCommand() {
    String command = "";

    if(returnType.getKeyword() == Keyword.VOID) {
      command += VM.writePush("constant", 0);
    }

    return command + VM.writeReturn();
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        return passActive(passer.matchKeyword(activeToken, Keyword.RETURN));
      case 1:
        if (passer.matchSymbol(activeToken, Symbol.SEMI_COLON))
          return passActive(4);
        pos++;
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression();
        if (compileExpression != null)
          return handleSubroutine(compileExpression);
      case 3:
        return passActive(passer.matchSymbol(activeToken, Symbol.SEMI_COLON));
      case 4:
        return buildCommand() + endRoutine();
      default:
        return fail();
    }
  }
}
