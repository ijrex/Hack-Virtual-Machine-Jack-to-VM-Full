package compilationEngine;

import token.*;
import tokenlib.Keyword;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.Match;

public class CompileStatements extends Compile {

  Compile compileStatement;

  Token returnType;

  public CompileStatements(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "statements";
  }

  public CompileStatements(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable, Token _returnType) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "statements";
    returnType = _returnType;
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
              compileStatement = new CompileStatementReturn(tab, classSymbolTable, scopedSymbolTable, returnType);
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
