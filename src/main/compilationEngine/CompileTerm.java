package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileTerm extends Compile {

  Compile compileExpressionList;
  Compile compileExpression;
  Compile compileTerm;

  Token lookAhead;

  String unaryOpCommand;

  public CompileTerm(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "term";
  }

  private String buildConstCommand(Token token) {
    return "push constant " + token.getValue() + "\n"; 
  }

  private String buildUnaryOpCommand(Symbol symbol){
    String command = "";

    // Hardware command
    switch (symbol) {
      case MINUS:
        command = "neg";
        break;
      case TILDE:
        command = "@todo - handle tilde";
        break;
      default:
        break;
    }

    return command + "\n";

  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (Match.intConst(token) || Match.stringConst(token) || Match.keywordConst(token)) {
          String command = buildConstCommand(token);
          return parseToken(command, token, true, 500);
        }
        if (Match.identifier(token)) {
          lookAhead = token;
          pos++;
          return "";
        }
        if (Match.symbol(token, Symbol.PARENTHESIS_L))
          return parseToken(token, true, 300);

        if (Match.unaryOp(token)) {
          unaryOpCommand = buildUnaryOpCommand(token.getSymbol());
          return parseToken(token, true, 400);
        }

        return fail();
      case 1:
        if (lookAhead != null) {
          Symbol symbol = token.getSymbol();

          switch (symbol) {
            case PERIOD:
              handleIdentifierClassOrVarName(lookAhead);
              return parseToken(lookAhead, true) + parseToken(token, true, 100);
            case BRACKET_L:
              handleIdentifierVarName(lookAhead);
              return parseToken(lookAhead, true) + parseToken(token, true, 200);
            case PARENTHESIS_L:
              lookAhead.setIdentifierCat(IdentifierCat.SUBROUTINE);
              return parseToken(lookAhead, true) + parseToken(token, true, 102);
            default:
              handleIdentifierVarName(lookAhead);
              return parseToken(lookAhead, true) + postfix();
          }
        }
        return fail();
      case 100:
        if(Match.identifier(token)) {
          token.setIdentifierCat(IdentifierCat.SUBROUTINE);
          return parseToken(token, true);
        }
        return fail();
      case 101:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 102:
        if (compileExpressionList == null)
          compileExpressionList = new CompileExpressionList(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileExpressionList, token);
      case 103:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 104:
        return postfix();

      case 200:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileExpression, token);
      case 201:
        return parseToken(token, Match.symbol(token, Symbol.BRACKET_R));
      case 202:
        return postfix();

      case 300:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileExpression, token);
      case 301:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 302:
        return postfix();

      case 400:
        if (compileTerm == null)
          compileTerm = new CompileTerm(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileTerm, token);
      case 401:
        return unaryOpCommand + postfix();

      case 500:
        return postfix();

      default:
        return fail();
    }
  }
}
