package compilationEngine;

import tokenlib.Symbol;

import java.io.IOException;

public class CompileExpressionList extends Compile {

  Compile compileExpression;

  int numArgs = 0;

  public CompileExpressionList() {
    routineLabel = "expressionList";
  }

  public int getNumArgs() {
    return numArgs;
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        if (passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R))
          return endRoutine();
        pos++;
      case 1:
        if (compileExpression == null)
          compileExpression = new CompileExpression();
        return handleSubroutine(compileExpression);
      case 2:
        numArgs++;
        if (passer.matchSymbol(activeToken, Symbol.COMMA)) {
          compileExpression = null;
          return passActive(0);
        }
        return endRoutine();
      default:
        return fail();
    }
  }
}
