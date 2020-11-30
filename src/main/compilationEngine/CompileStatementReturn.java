package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.Match;

public class CompileStatementReturn extends Compile {

  Compile compileExpression;

  public CompileStatementReturn(int _tab, SymbolTable _classSymbolTable) {
    super(_tab, _classSymbolTable);
    wrapperLabel = "returnStatement";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.RETURN));
      case 1:
        if (Match.symbol(token, Symbol.SEMI_COLON))
          return parseToken(token, true, 4);
        pos++;
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab, classSymbolTable);
        if (compileExpression != null)
          return handleChildClass(compileExpression, token);
      case 3:
        return parseToken(token, Match.symbol(token, Symbol.SEMI_COLON));
      case 4:
        return postfix();
      default:
        return fail();
    }
  }
}
