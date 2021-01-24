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

  public CompileStatementIf(int _tab) {
    super(_tab);
    wrapperLabel = "ifStatement";

    numIfStatements++;
  }

  String labelTrue = "IF_TRUE" + numIfStatements;
  String labelFalse = "IF_FALSE" + numIfStatements;
  String labelEnd = "IF_END" + numIfStatements;

  String ifStartCommand = VM.writeIf(labelTrue) + VM.writeGoto(labelFalse) + VM.writeLabel(labelTrue);

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return passToken(token, Match.keyword(token, Keyword.IF));
      case 1:
        return passToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab);
        return handleChildClass(compileExpression, token);
      case 3:
        return passToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 4:
        return ifStartCommand + passToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 5:
        if (compileStatements1 == null)
          compileStatements1 = new CompileStatements(tab);
        return handleChildClass(compileStatements1, token);
      case 6:
        return passToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 7:
        if (Match.keyword(token, Keyword.ELSE)) {
          return VM.writeGoto(labelEnd) + VM.writeLabel(labelFalse) + passToken(token, true, 8);
        }
        return VM.writeLabel(labelFalse) + postfix();
      case 8:
        return passToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 9:
        if (compileStatements2 == null)
          compileStatements2 = new CompileStatements(tab);
        return handleChildClass(compileStatements2, token);
      case 10:
        return passToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 11:
        return VM.writeLabel(labelEnd) + postfix();
      default:
        return fail();
    }
  }
}
