package compilationenginexml;

import token.*;
import tokenlib.Keyword;

import java.io.IOException;

import compilationenginexml.symboltable.SymbolTable;
import compilationenginexml.util.Match;

public class CompileStatements extends Compile {

  Compile compileStatement;

  public CompileStatements(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "statements";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (Match.isStatementDec(token) && compileStatement == null) {
          Keyword statementType = token.getKeyword();

          switch (statementType) {
            case LET:
              compileStatement = new CompileStatementLet(tab, classSymbolTable, scopedSymbolTable);
              break;
            case IF:
              compileStatement = new CompileStatementIf(tab, classSymbolTable, scopedSymbolTable);
              break;
            case WHILE:
              compileStatement = new CompileStatementWhile(tab, classSymbolTable, scopedSymbolTable);
              break;
            case DO:
              compileStatement = new CompileStatementDo(tab, classSymbolTable, scopedSymbolTable);
              break;
            case RETURN:
              compileStatement = new CompileStatementReturn(tab, classSymbolTable, scopedSymbolTable);
              break;
            default:
              fail();
          }
        }
        if (compileStatement != null)
          return handleChildClass(compileStatement, token);
      case 1:
        if (Match.isStatementDec(token) && compileStatement != null) {
          compileStatement = null;
          pos--;
          return handleToken(token);
        }
      default:
        return postfix();
    }
  }

}
