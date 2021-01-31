package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.vmwriter.VM;

public class CompileStatementDo extends Compile {

  Compile compileExpressionList;

  public CompileStatementDo() {
    routineLabel = "doStatement";
  }

  int nArgs;

  // Token lookahead;
  Token subroutine;

  SymbolEntry lookaheadSymbol;

  String subroutineCallName;
  String callClassName;

  private String buildLocalCommandStart() {
    // Local method or function call
    // i.e. foo();
    nArgs++;
    callClassName = className;
    subroutineCallName = lookaheadSymbol.getName();
    return VM.writePush("pointer", 0);
  }

  private String buildRemoteCommandStart() {

    subroutineCallName = subroutine.getValue();

    // Remote method call
    // i.e. foo.bar();
    if (lookaheadSymbol.getKey() >= 0) {
      nArgs++;
      callClassName = lookaheadSymbol.getType();
      String location = VM.parseLocation(lookaheadSymbol.getKind());
      return VM.writePush(location, lookaheadSymbol.getKey());
    }

    // Remote function call
    // i.e. Foo.bar();
    callClassName = lookaheadSymbol.getName();

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
          lookaheadSymbol = findSymbolOrPlaceholder(activeToken);
          return passActive();
        }
        return fail();
      case 2:
        if (passer.matchSymbol(activeToken, Symbol.PARENTHESIS_L)) {
          return buildLocalCommandStart() + passActive(5);
        }
        if (passer.matchSymbol(activeToken, Symbol.PERIOD)) {
          return passActive();
        }
        return fail();
      case 3:
        if (passer.isIdentifier(activeToken)) {
          subroutine = activeToken;
          return buildRemoteCommandStart() + passActive();
        }
        return fail();
      case 4:
        return passActive(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_L));
      case 5:
        if (compileExpressionList == null)
          compileExpressionList = new CompileExpressionList();
        return handleSubroutine(compileExpressionList);
      case 6:
        return passActive(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R));
      case 7:
        return passActive(passer.matchSymbol(activeToken, Symbol.SEMI_COLON));
      case 8:
        nArgs += compileExpressionList.getNumArgs();
        return buildCommandEnd() + endRoutine();
      default:
        return fail();
    }
  }
}
