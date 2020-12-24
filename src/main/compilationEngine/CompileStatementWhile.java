package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileStatementWhile extends Compile {

  Compile compileExpression;
  Compile compileStatements;

  String expressionName = "WHILE_EXP";
  String expressionEndName = "WHILE_END";

  public CompileStatementWhile(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "whileStatement";

    expressionName = expressionName + whileExpressionCount;
    expressionEndName = expressionEndName + whileExpressionCount;
    whileExpressionCount++;
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        String labelCommand = "label " + expressionName + "\n";
        return labelCommand + parseToken(token, Match.keyword(token, Keyword.WHILE));
      case 1:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileExpression, token);
      case 3:
        String evaluateExpressionCommand = "not\n";
        evaluateExpressionCommand += "if-goto " + expressionEndName + "\n";
        return evaluateExpressionCommand + parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 5:
        if (compileStatements == null)
          compileStatements = new CompileStatements(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileStatements, token);
      case 6:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 7:
        String returnToStartCommand = "goto " + expressionName + "\n";
        returnToStartCommand += "label " + expressionEndName + "\n";
        return returnToStartCommand + postfix();
      default:
        return fail();
    }
  }
}
