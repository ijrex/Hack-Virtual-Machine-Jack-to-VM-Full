package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.*;

public class CompileExpressionList extends Compile {

  Compile compileExpression;

  int numArgs = 0;

  public CompileExpressionList(int _tab) {
    super(_tab);
    wrapperLabel = "expressionList";
  }

  public int getNumArgs() {
    return numArgs;
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -2:
        return postfix();
      case -1:
        if (Match.symbol(token, Symbol.PARENTHESIS_R))
          return prefix(token, -2);
        return prefix(token);
      case 0:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab);
        return handleChildClass(compileExpression, token);
      case 1:
        numArgs++;
        if (Match.symbol(token, Symbol.COMMA)) {
          compileExpression = null;
          pos--;
          return passToken(token, true, 0);
        }
        return postfix();
      default:
        return fail();
    }
  }
}
