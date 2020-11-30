package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.Match;

public class CompileStatementDo extends Compile {

  Compile compileExpressionList;

  public CompileStatementDo(int _tab, SymbolTable _classSymbolTable) {
    super(_tab, _classSymbolTable);
    wrapperLabel = "doStatement";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.DO));
      case 1:
        return parseToken(token, Match.identifier(token));
      case 2:
        if (Match.symbol(token, Symbol.PARENTHESIS_L))
          return parseToken(token, true, 5);
        if (Match.symbol(token, Symbol.PERIOD))
          return parseToken(token, true);
        return fail();
      case 3:
        return parseToken(token, Match.identifier(token));
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 5:
        if (compileExpressionList == null)
          compileExpressionList = new CompileExpressionList(tab, classSymbolTable);
        return handleChildClass(compileExpressionList, token);
      case 6:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 7:
        return parseToken(token, Match.symbol(token, Symbol.SEMI_COLON));
      case 8:
        return postfix();
      default:
        return fail();
    }
  }
}
