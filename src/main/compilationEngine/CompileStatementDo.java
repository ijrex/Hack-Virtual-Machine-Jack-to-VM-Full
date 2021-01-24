package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.Match;
import compilationEngine.vmwriter.VM;

public class CompileStatementDo extends Compile {

  Compile compileExpressionList;

  public CompileStatementDo(int _tab) {
    super(_tab);
    wrapperLabel = "doStatement";
  }

  int nArgs;

  Token lookahead;
  Token subroutine;

  String subroutineCallName;
  String callClassName;

  private String buildLocalCommandStart() {
    // Local method or function call
    // i.e. foo();
    nArgs++;
    callClassName = className;
    subroutineCallName = lookahead.getValue();
    return VM.writePush("pointer", 0);
  }

  private String buildRemoteCommandStart() {

    subroutineCallName = subroutine.getValue();

    // Remote method call
    // i.e. foo.bar();
    if (lookahead.getRunningIndex() >= 0) {
      nArgs++;
      callClassName = lookahead.getVarType();
      String location = VM.parseLocation(lookahead.getIdentifierCat());
      return VM.writePush(location, lookahead.getRunningIndex());
    }

    // Remote function call
    // i.e. Foo.bar();
    callClassName = lookahead.getValue();

    return "";
  }

  private String buildCommandEnd() {
    String subroutineCall = VM.createSubroutineName(callClassName, subroutineCallName);

    String command = VM.writeCall(subroutineCall, nArgs);
    command += VM.writePop("temp", 0);
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
          return parseToken(token, true);
        }
        return fail();
      case 2:
        if (Match.symbol(token, Symbol.PARENTHESIS_L)) {
          lookahead.setIdentifierCat(IdentifierCat.SUBROUTINE);
          return buildLocalCommandStart() + parseToken(token, true, 5);
        }
        if (Match.symbol(token, Symbol.PERIOD)) {
          handleIdentifierClassOrVarName(lookahead);
          return parseToken(token, true);
        }
        return fail();
      case 3:
        if (Match.identifier(token)) {
          token.setIdentifierCat(IdentifierCat.SUBROUTINE);
          subroutine = token;
          return buildRemoteCommandStart() + parseToken(token, true);
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
        return parseToken(token, Match.symbol(token, Symbol.SEMI_COLON));
      case 8:
        nArgs += compileExpressionList.getNumArgs();
        return buildCommandEnd() + postfix();
      default:
        return fail();
    }
  }
}
