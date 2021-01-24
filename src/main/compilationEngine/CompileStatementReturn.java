package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.Match;
import compilationEngine.vmwriter.VM;

public class CompileStatementReturn extends Compile {

  Compile compileExpression;

  public CompileStatementReturn(int _tab) {
    super(_tab);
    wrapperLabel = "returnStatement";
  }

  private String buildCommand() {
    String command = "";

    if(returnType.getKeyword() == Keyword.VOID) {
      command += VM.writePush("constant", 0);
    }

    return command + VM.writeReturn();
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return passToken(token, Match.keyword(token, Keyword.RETURN));
      case 1:
        if (Match.symbol(token, Symbol.SEMI_COLON))
          return passToken(token, true, 4);
        pos++;
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab);
        if (compileExpression != null)
          return handleChildClass(compileExpression, token);
      case 3:
        return passToken(token, Match.symbol(token, Symbol.SEMI_COLON));
      case 4:
        return buildCommand() + postfix();
      default:
        return fail();
    }
  }
}
