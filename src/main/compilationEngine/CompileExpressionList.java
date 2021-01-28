package compilationEngine;

import tokenlib.Symbol;

import java.io.IOException;

public class CompileExpressionList extends Compile {

  Compile compileExpression;

  int numArgs = 0;

  public CompileExpressionList() {
    wrapperLabel = "expressionList";
  }

  public int getNumArgs() {
    return numArgs;
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case -2:
        return postfix();
      case -1:
        if (passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R))
          return prefix(-2);
        return prefix();
      case 0:
        if (compileExpression == null)
          compileExpression = new CompileExpression();
        return handleChildClass(compileExpression);
      case 1:
        numArgs++;
        if (passer.matchSymbol(activeToken, Symbol.COMMA)) {
          compileExpression = null;
          pos--;
          return passToken(0);
        }
        return postfix();
      default:
        return fail();
    }
  }
}
