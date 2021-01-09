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

  String command;

  private String buildCommand(Token token) {
    String location = "";

    IdentifierCat cat = token.getIdentifierCat();

    switch (cat) {
      case ARGUMENT:
        location = "argument";
        break;
      case VAR:
        location = "local";
        break;
      default:
        location = "@todo: unhandled " + cat;
        break;
    }

    return VM.writePop(location, token.getRunningIndex());
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.LET));
      case 1:
        if (Match.identifier(token)) {
          handleIdentifierVarName(token);
          command = buildCommand(token);
          return parseToken(token, true);
        }
        return fail();
      case 2:
        if (compileExpression1 == null && Match.symbol(token, Symbol.BRACKET_L)) {
          compileExpression1 = new CompileExpression(tab);
          return parseToken(token, true, 2);
        }
        if (compileExpression1 != null)
          return handleChildClass(compileExpression1, token);
        pos++;
      case 3:
        if (compileExpression1 != null)
          return parseToken(token, Match.symbol(token, Symbol.BRACKET_R));
        pos++;
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.EQUALS));
      case 5:
        if (compileExpression2 == null)
          compileExpression2 = new CompileExpression(tab);
        return handleChildClass(compileExpression2, token);
      case 6:
        return parseToken(token, Match.symbol(token, Symbol.SEMI_COLON));
      case 7:
        return command + postfix();
      default:
        return fail();
    }
  }
}
