package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.*;
import compilationEngine.vmwriter.VM;

public class CompileStatementWhile extends Compile {

  Compile compileExpression;
  Compile compileStatements;

  public CompileStatementWhile() {
    wrapperLabel = "whileStatement";

    numWhileStatements++;
  }

  String labelExp = "WHILE_EXP" + numWhileStatements;
  String labelEnd = "WHILE_END" + numWhileStatements;

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return VM.writeLabel(labelExp) + passToken(token, Match.keyword(token, Keyword.WHILE));
      case 1:
        return passToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression();
        return handleChildClass(compileExpression, token);
      case 3:
        return passToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 4:
        return "not\n" + VM.writeIf(labelEnd) + passToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 5:
        if (compileStatements == null)
          compileStatements = new CompileStatements();
        return handleChildClass(compileStatements, token);
      case 6:
        return passToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 7:
        return VM.writeGoto(labelExp) + VM.writeLabel(labelEnd) + postfix();
      default:
        return fail();
    }
  }
}
