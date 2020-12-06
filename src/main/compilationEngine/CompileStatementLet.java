package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileStatementLet extends Compile {

  Compile compileExpression1;
  Compile compileExpression2;

  public CompileStatementLet(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "letStatement";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.LET));
      case 1:
        if(Match.identifier(token)) {
          this.handleIdentifierFromSymbolTable(token);
          return parseToken(token, true);
        }
        return fail();
      case 2:
        if (compileExpression1 == null && Match.symbol(token, Symbol.BRACKET_L)) {
          compileExpression1 = new CompileExpression(tab, classSymbolTable, scopedSymbolTable);
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
          compileExpression2 = new CompileExpression(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileExpression2, token);
      case 6:
        return parseToken(token, Match.symbol(token, Symbol.SEMI_COLON));
      case 7:
        return postfix();
      default:
        return fail();
    }
  }
}
