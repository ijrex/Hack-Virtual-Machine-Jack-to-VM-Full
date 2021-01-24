package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.*;
import compilationEngine.vmwriter.VM;

public class CompileStatementLet extends Compile {

  Compile compileExpression1;
  Compile compileExpression2;

  public CompileStatementLet(int _tab) {
    super(_tab);
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

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return passToken(token, Match.keyword(token, Keyword.LET));
      case 1:
        if (Match.identifier(token)) {
          handleIdentifierVarName(token);
          varToken = token;
          return passToken(token, true);
        }
        return fail();
      case 2:
        if (compileExpression1 == null && Match.symbol(token, Symbol.BRACKET_L)) {
          isArray = true;
          compileExpression1 = new CompileExpression(tab);
          return passToken(token, true, 2);
        }
        if (compileExpression1 != null)
          return handleChildClass(compileExpression1, token);
        pos++;
      case 3:
        if (compileExpression1 != null){
          String command = buildArrayLocationCommand();
          return command + passToken(token, Match.symbol(token, Symbol.BRACKET_R));
        }
        pos++;
      case 4:
        return passToken(token, Match.symbol(token, Symbol.EQUALS));
      case 5:
        if (compileExpression2 == null)
          compileExpression2 = new CompileExpression(tab);
        return handleChildClass(compileExpression2, token);
      case 6:
        return passToken(token, Match.symbol(token, Symbol.SEMI_COLON));
      case 7:
        return buildAssignmentCommand() + postfix();
      default:
        return fail();
    }
  }
}
