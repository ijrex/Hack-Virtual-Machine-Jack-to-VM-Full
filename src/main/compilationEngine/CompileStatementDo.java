package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.Match;

public class CompileStatementDo extends Compile {

  Compile compileExpressionList;

  public CompileStatementDo(int _tab) {
    super(_tab);
    wrapperLabel = "doStatement";
  }

  Token lookahead;
  Token subroutineCall;

  private String buildCommand(int _numArgs) {

    String command = "";

    String arg1 = lookahead.getValue();
    String arg2 = "";
    int numArgs = _numArgs;

    int runningIndex = lookahead.getRunningIndex();

    if (runningIndex >= 0 && !lookahead.getIsPrimitiveType()) {
      arg1 = lookahead.getIdentifierType();
      numArgs++;
      command += "push " + parseTokenCategory(lookahead.getIdentifierCat()) + " " + lookahead.getRunningIndex() + "\n";
    }

    if (subroutineCall != null) {
      arg2 = "." + subroutineCall.getValue();
    }

    command += "call " + arg1 + arg2 + " " + numArgs + "\n";
    command += "pop temp 0\n";
    return command;
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.DO));
      case 1:
        if (Match.identifier(token)) {
          lookahead = token;
          pos++;
          return "";
        }
        return fail();
      case 2:
        if (Match.symbol(token, Symbol.PARENTHESIS_L)) {
          lookahead.setIdentifierCat(IdentifierCat.SUBROUTINE);
          return parseToken(lookahead, true) + parseToken(token, true, 5);
        }
        if (Match.symbol(token, Symbol.PERIOD)) {
          handleIdentifierClassOrVarName(lookahead);
          return parseToken(lookahead, true, 2) + parseToken(token, true);
        }
        return fail();
      case 3:
        if (Match.identifier(token)) {
          subroutineCall = token;
          token.setIdentifierCat(IdentifierCat.SUBROUTINE);
          return parseToken(token, true);
        }
        return fail();
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 5:
        if (compileExpressionList == null)
          compileExpressionList = new CompileExpressionList(tab);
        return handleChildClass(compileExpressionList, token);
      case 6:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 7:
        int numArgs = compileExpressionList.getNumArgs();
        String command = buildCommand(numArgs);
        return parseToken(command, token, Match.symbol(token, Symbol.SEMI_COLON));
      case 8:
        return postfix();
      default:
        return fail();
    }
  }
}
