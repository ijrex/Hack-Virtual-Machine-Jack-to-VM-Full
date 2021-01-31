package compilationEngine;

import tokenlib.Keyword;

import java.io.IOException;

public class CompileStatements extends Compile {

  Compile compileStatement;

  public CompileStatements() {
    routineLabel = "statements";
  }

  protected String handleRoutine() throws IOException {
    switch (pos) {
      case 0:
        if (passer.isStatementDec(activeToken) && compileStatement == null) {
          Keyword statementType = activeToken.getKeyword();

          switch (statementType) {
            case LET:
              compileStatement = new CompileStatementLet();
              break;
            case IF:
              compileStatement = new CompileStatementIf();
              break;
            case WHILE:
              compileStatement = new CompileStatementWhile();
              break;
            case DO:
              compileStatement = new CompileStatementDo();
              break;
            case RETURN:
              compileStatement = new CompileStatementReturn();
              break;
            default:
              fail();
          }
        }
        if (compileStatement != null)
          return handleSubroutine(compileStatement);
      case 1:
        if (passer.isStatementDec(activeToken) && compileStatement != null) {
          compileStatement = null;
          pos--;
          return handleRoutine();
        }
      default:
        return endRoutine();
    }
  }

}
