package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.vmwriter.VM;

public class CompileStatementDo extends Compile {

  Compile compileExpressionList;

  public CompileStatementDo() {
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

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        return passActive(passer.matchKeyword(activeToken, Keyword.DO));
      case 1:
        if (passer.isIdentifier(activeToken)){
          lookahead = activeToken;
          return passActive();
        }
        return fail();
      case 2:
        if (passer.matchSymbol(activeToken, Symbol.PARENTHESIS_L)) {
          lookahead.setIdentifierCat(IdentifierCat.SUBROUTINE);
          return buildLocalCommandStart() + passActive(5);
        }
        if (passer.matchSymbol(activeToken, Symbol.PERIOD)) {
          handleIdentifierClassOrVarName(lookahead);
          return passActive();
        }
        return fail();
      case 3:
        if (passer.isIdentifier(activeToken)) {
          activeToken.setIdentifierCat(IdentifierCat.SUBROUTINE);
          subroutine = activeToken;
          return buildRemoteCommandStart() + passActive();
        }
        return fail();
      case 4:
        return passActive(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_L));
      case 5:
        if (compileExpressionList == null)
          compileExpressionList = new CompileExpressionList();
        return handleChildClass(compileExpressionList);
      case 6:
        return passActive(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R));
      case 7:
        return passActive(passer.matchSymbol(activeToken, Symbol.SEMI_COLON));
      case 8:
        nArgs += compileExpressionList.getNumArgs();
        return buildCommandEnd() + postfix();
      default:
        return fail();
    }
  }
}
