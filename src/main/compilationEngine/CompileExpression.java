package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.*;
import compilationEngine.vmwriter.VM;

public class CompileExpression extends Compile {

  Compile compileTerm1;
  Compile compileTerm2;

  String command = "";

  public static String buildCommand(Token token) {
    Symbol value = token.getSymbol();

    // Hardware command
    switch (value) {
      case PLUS:
      case MINUS:
      case MORE_THAN:
      case LESS_THAN:
      case AMPERSAND:
      case EQUALS:
      case PIPE:
        return VM.writeArithmetic(value);
      default:
        break;
    }

    // Software command (Math lib)
    String output;

    switch (value) {
      case ASTERISK:
        output = "multiply";
        break;
      case SLASH_FWD:
        output = "divide";
        break;
      default:
        output = "@todo parse " + token.getSymbol();
        break;
    }

    return VM.writeCall("Math." + output, 2);
  }

  public CompileExpression(int _tab) {
    super(_tab);
    wrapperLabel = "expression";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (compileTerm1 == null)
          compileTerm1 = new CompileTerm(tab);
        return handleChildClass(compileTerm1, token);
      case 1:
        if (Match.op(token)) {
          compileTerm1 = null;
          command = buildCommand(token) + command;
          return parseToken(token, true, 0);
        }
        return command + postfix();
      default:
        return fail();
    }
  }
}
