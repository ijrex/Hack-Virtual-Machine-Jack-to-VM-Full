package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.vmwriter.VM;

public class CompileExpression extends Compile {

  Compile compileTerm1;
  Compile compileTerm2;

  String command = "";

  public CompileExpression() {
    routineLabel = "expression";
  }

  private String buildCommand(Token token) throws IOException {
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
        throw new IOException(compileError("Invalid arithmetic symbol"));
    }

    return VM.writeCall("Math." + output, 2);
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        if (compileTerm1 == null)
          compileTerm1 = new CompileTerm();
        return handleSubroutine(compileTerm1);
      case 1:
        if (passer.isOp(activeToken)){
          compileTerm1 = null;
          command = buildCommand(activeToken) + command;
          return passActive(0);
        }
        return command + endRoutine();
      default:
        return fail();
    }
  }
}
