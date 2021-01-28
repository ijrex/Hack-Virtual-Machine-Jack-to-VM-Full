package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.vmwriter.VM;

public class CompileStatementLet extends Compile {

  Compile compileExpression1;
  Compile compileExpression2;

  public CompileStatementLet() {
    wrapperLabel = "letStatement";
  }

  Token varToken;
  Boolean isArray = false;


  private String buildArrayLocationCommand() {
    String location = VM.parseLocation(varToken.getIdentifierCat());
    String command = VM.writePush(location, varToken.getRunningIndex());
    command += "add\n";
    return command;
  }

  private String buildAssignmentCommand() {
    if(isArray) {
      String command = VM.writePop("temp", 0);
      command += VM.writePop("pointer", 1);
      command += VM.writePush("temp", 0);
      command += VM.writePop("that", 0);
      return command;
    }

    String location = VM.parseLocation(varToken.getIdentifierCat());
    return VM.writePop(location, varToken.getRunningIndex());
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case -1:
        return prefix();
      case 0:
        return passToken(passer.matchKeyword(activeToken, Keyword.LET));
      case 1:
        if (passer.isIdentifier(activeToken)) {
          handleIdentifierVarName(activeToken);
          varToken = activeToken;
          return passToken();
        }
        return fail();
      case 2:
        if (compileExpression1 == null && passer.matchSymbol(activeToken, Symbol.BRACKET_L)) {
          isArray = true;
          compileExpression1 = new CompileExpression();
          return passToken(2);
        }
        if (compileExpression1 != null)
          return handleChildClass(compileExpression1);
        pos++;
      case 3:
        if (compileExpression1 != null){
          String command = buildArrayLocationCommand();
          return command + passToken(passer.matchSymbol(activeToken, Symbol.BRACKET_R));
        }
        pos++;
      case 4:
        return passToken(passer.matchSymbol(activeToken, Symbol.EQUALS));
      case 5:
        if (compileExpression2 == null)
          compileExpression2 = new CompileExpression();
        return handleChildClass(compileExpression2);
      case 6:
        return passToken(passer.matchSymbol(activeToken, Symbol.SEMI_COLON));
      case 7:
        return buildAssignmentCommand() + postfix();
      default:
        return fail();
    }
  }
}
