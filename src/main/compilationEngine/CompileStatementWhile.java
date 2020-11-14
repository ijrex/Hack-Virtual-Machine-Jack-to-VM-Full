package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.*;

public class CompileStatementWhile extends Compile {

  Compile compileExpression;
  Compile compileStatements;

  public CompileStatementWhile(int _tab) {
    super(_tab);
    wrapperLabel = "whileStatement";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.WHILE));
      case 1:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab);
        return handleChildClass(compileExpression, token);
      case 3:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 5:
        if (compileStatements == null)
          compileStatements = new CompileStatements(tab);
        return handleChildClass(compileStatements, token);
      case 6:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 7:
        return postfix();
      default:
        return fail();
    }
  }
}
