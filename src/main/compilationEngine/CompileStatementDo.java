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

  private String buildCommand() {
    String command = "";
    String subroutineCallName = "";
    String callClassName = lookahead.getValue();

    // @todo: handle do statements with no subroutine
    if (subroutine != null) {
      subroutineCallName = subroutine.getValue();
    }

    int runningIndex = lookahead.getRunningIndex();

    if (runningIndex >= 0) {
      callClassName = lookahead.getVarType();

      nArgs++;

      IdentifierCat identifierCat = lookahead.getIdentifierCat();
      String location;

      switch (identifierCat) {
        case VAR:
          location = "local";
          break;
        default:
          location = "@todo: handle " + identifierCat.toString();
      }

      command += VM.writePush(location, runningIndex);
    }

    String subroutineCall = VM.createSubroutineName(callClassName, subroutineCallName);

    command += VM.writeCall(subroutineCall, nArgs);
    command += VM.writePop("temp", 0);
    return command;
  }

  // private String handleSubroutineName(Token className, Token subroutineName) {

  // if (lookahead.getRunningIndex() >= 0) {
  // return "@todo: handle class do call " + className.getValue() + ", " +
  // subroutineName.getValue() + ",";
  // }

  // return VM.createSubroutineName(className.getValue(),
  // subroutineName.getValue());
  // }

  // private String buildCommand() {
  // String command = "";

  // String classCallName = lookahead.getValue();
  // int runningIndex = lookahead.getRunningIndex();

  // System.out.println(classCallName);
  // System.out.println(runningIndex);
  // System.out.println(subroutine.getValue());

  // if (runningIndex >= 0) {

  // classCallName = lookahead.getVarType();
  // command += VM.writePush("aa", runningIndex);
  // }

  // String subroutineCall = VM.createSubroutineName(classCallName, "test");

  // command += VM.writeCall(subroutineCall, nArgs);
  // command += VM.writePop("temp", 0);
  // return command;
  // }

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
          token.setIdentifierCat(IdentifierCat.SUBROUTINE);
          subroutine = token;
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
        return parseToken(token, Match.symbol(token, Symbol.SEMI_COLON));
      case 8:
        nArgs = compileExpressionList.getNumArgs();
        return buildCommand() + postfix();
      default:
        return fail();
    }
  }
}
