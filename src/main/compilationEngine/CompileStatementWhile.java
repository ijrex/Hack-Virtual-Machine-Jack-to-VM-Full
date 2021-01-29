package compilationEngine;

import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.vmwriter.VM;

public class CompileStatementWhile extends Compile {

  Compile compileExpression;
  Compile compileStatements;

  public CompileStatementWhile() {
    routineLabel = "whileStatement";

    numWhileStatements++;
  }

  String labelExp = "WHILE_EXP" + numWhileStatements;
  String labelEnd = "WHILE_END" + numWhileStatements;

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        return VM.writeLabel(labelExp) + passActive(passer.matchKeyword(activeToken, Keyword.WHILE));
      case 1:
        return passActive(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_L));
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression();
        return handleChildClass(compileExpression);
      case 3:
        return passActive(passer.matchSymbol(activeToken, Symbol.PARENTHESIS_R));
      case 4:
        return "not\n" + VM.writeIf(labelEnd) + passActive(passer.matchSymbol(activeToken, Symbol.BRACE_L));
      case 5:
        if (compileStatements == null)
          compileStatements = new CompileStatements();
        return handleChildClass(compileStatements);
      case 6:
        return passActive(passer.matchSymbol(activeToken, Symbol.BRACE_R));
      case 7:
        return VM.writeGoto(labelExp) + VM.writeLabel(labelEnd) + endRoutine();
      default:
        return fail();
    }
  }
}
