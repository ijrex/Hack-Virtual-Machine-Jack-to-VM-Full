package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileStatementIf extends Compile {

  Compile compileExpression;
  Compile compileStatements1;
  Compile compileStatements2;

  String expressionTrueName = "IF_TRUE";
  String expressionFalseName = "IF_FALSE";
  String expressionEndName = "IF_END";

  Token returnType;

  public CompileStatementIf(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable,
      Token _returnType) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "ifStatement";

    returnType = _returnType;

    expressionTrueName = expressionTrueName + ifExpressionCount;
    expressionFalseName = expressionFalseName + ifExpressionCount;
    expressionEndName = expressionEndName + ifExpressionCount;
    ifExpressionCount++;
  }

  private String buildIfCommand() {
    String command = "if-goto " + expressionTrueName + "\n";
    command += "goto " + expressionFalseName + "\n";
    command += "label " + expressionTrueName + "\n";
    return command;
  }

  private String buildElseCommand() {
    String command = "goto " + expressionEndName + "\n";
    command += "label " + expressionFalseName + "\n";
    return command;
  }

  private String buildIfEndCommand() {
    return "label " + expressionEndName + "\n";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.IF));
      case 1:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 2:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileExpression, token);
      case 3:
        return buildIfCommand() + parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 4:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 5:
        if (compileStatements1 == null)
          compileStatements1 = new CompileStatements(tab, classSymbolTable, scopedSymbolTable, returnType);
        return handleChildClass(compileStatements1, token);
      case 6:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 7:
        if (Match.keyword(token, Keyword.ELSE))
          return buildElseCommand() + parseToken(token, true, 8);
        return postfix();
      case 8:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 9:
        if (compileStatements2 == null)
          compileStatements2 = new CompileStatements(tab, classSymbolTable, scopedSymbolTable, returnType);
        return handleChildClass(compileStatements2, token);
      case 10:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_R));
      case 11:
        return buildIfEndCommand() + postfix();
      default:
        return fail();
    }
  }
}
