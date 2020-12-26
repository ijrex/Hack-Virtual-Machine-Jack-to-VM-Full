package compilationEngine;

import token.*;
import tokenlib.Keyword;

import java.io.IOException;

import compilationEngine.util.Match;

public class CompileStatements extends Compile {

  Compile compileStatement;

  public CompileStatements(int _tab) {
    super(_tab);
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
              compileStatement = new CompileStatementLet(tab);
              break;
            case IF:
              compileStatement = new CompileStatementIf(tab);
              break;
            case WHILE:
              compileStatement = new CompileStatementWhile(tab);
              break;
            case DO:
              compileStatement = new CompileStatementDo(tab);
              break;
            case RETURN:
              compileStatement = new CompileStatementReturn(tab);
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
