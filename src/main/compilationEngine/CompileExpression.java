package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileExpression extends Compile {

  Compile compileTerm1;
  Compile compileTerm2;

  String command;

  public CompileExpression(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "expression";
  }

  public static String buildOpCommand(Token token) {
    Symbol value = token.getSymbol();
    String output;

    // Hardware command
    switch (value) {
      case PLUS:
        return "add";
      default:
        break;
    }

    // Software command (Math lib)
    switch (value) {
      case ASTERISK:
        output = "multiply";
        break;
      default:
        return null;
    }

    return "call Math." + output + " 2";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (compileTerm1 == null)
          compileTerm1 = new CompileTerm(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileTerm1, token);
      case 1:
        if (Match.op(token)) {
          compileTerm1 = null;
          command = buildOpCommand(token) + "\n";
          return parseToken(token, true, 0);
        }
        return command + postfix();
      default:
        return fail();
    }
  }
}
