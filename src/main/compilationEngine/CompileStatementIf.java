package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.*;
import compilationEngine.vmwriter.VM;

public class CompileStatementIf extends Compile {

  Compile compileExpression;
  Compile compileStatements1;
  Compile compileStatements2;

  public CompileStatementIf() {
    wrapperLabel = "ifStatement";

    numIfStatements++;
  }

  String labelTrue = "IF_TRUE" + numIfStatements;
  String labelFalse = "IF_FALSE" + numIfStatements;
  String labelEnd = "IF_END" + numIfStatements;

  String ifStartCommand = VM.writeIf(labelTrue) + VM.writeGoto(labelFalse) + VM.writeLabel(labelTrue);

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case -1:
        return prefix();
      case 0:
        return passToken(passer.matchKeyword(activeToken, Keyword.IF));
      case 1:
        return passToken(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_L));
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression();
        return handleChildClass(compileExpression);
      case 3:
        return passToken(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R));
      case 4:
        return ifStartCommand + passToken(passer.matchSymbol(activeToken, Symbol.BRACE_L));
      case 5:
        if (compileStatements1 == null)
          compileStatements1 = new CompileStatements();
        return handleChildClass(compileStatements1);
      case 6:
        return passToken(passer.matchSymbol(activeToken, Symbol.BRACE_R));
      case 7:
        if (passer.matchKeyword(activeToken, Keyword.ELSE)) {
          return VM.writeGoto(labelEnd) + VM.writeLabel(labelFalse) + passToken(8);
        }
        return VM.writeLabel(labelFalse) + postfix();
      case 8:
        return passToken(passer.matchSymbol(activeToken, Symbol.BRACE_L));
      case 9:
        if (compileStatements2 == null)
          compileStatements2 = new CompileStatements();
        return handleChildClass(compileStatements2);
      case 10:
        return passToken(passer.matchSymbol(activeToken, Symbol.BRACE_R));
      case 11:
        return VM.writeLabel(labelEnd) + postfix();
      default:
        return fail();
    }
  }
}
