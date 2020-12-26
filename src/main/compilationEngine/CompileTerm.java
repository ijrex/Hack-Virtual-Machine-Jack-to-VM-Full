package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;
import tokenlib.TokenType;

import java.io.IOException;

import compilationEngine.util.*;

public class CompileTerm extends Compile {

  Compile compileExpressionList;
  Compile compileExpression;
  Compile compileTerm;

  Token lookAhead;

  String unaryOpCommand;
  String subroutineCommand = "call ";

  public CompileTerm(int _tab) {
    super(_tab);
    wrapperLabel = "term";
  }

  private String buildConstCommand(Token token) {
    String command = "";

    TokenType type = token.getType();

    if (type == TokenType.INT_CONST) {
      command = token.getValue();
    }

    if (type == TokenType.KEYWORD) {
      Keyword keyword = token.getKeyword();

      switch (keyword) {
        case TRUE:
          command = "0\n";
          command += "not";
          break;
        case FALSE:
          command += "0";
          break;
        default:
          break;
      }
    }

    return "push constant " + command + "\n";
  }

  private String buildUnaryOpCommand(Symbol symbol) {
    String command = "";

    // Hardware command
    switch (symbol) {
      case MINUS:
        command = "neg";
        break;
      case TILDE:
        command = "not";
        break;
      default:
        break;
    }

    return command + "\n";
  }

  private String buildSubroutineCallCommand(String command, int args) {
    return command + " " + args + "\n";
  }

  private String buildIdentifierCommand(Token token) {
    return "push " + parseTokenCategory(token.getIdentifierCat()) + " " + token.getRunningIndex() + "\n";
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
              subroutineCommand += lookAhead.getValue() + ".";
              return parseToken(lookAhead, true) + parseToken(token, true, 100);
            case BRACKET_L:
              handleIdentifierVarName(lookAhead);
              return parseToken(lookAhead, true) + parseToken(token, true, 200);
            case PARENTHESIS_L:
              lookAhead.setIdentifierCat(IdentifierCat.SUBROUTINE);
              return parseToken(lookAhead, true) + parseToken(token, true, 102);
            default:
              handleIdentifierVarName(lookAhead);
              return buildIdentifierCommand(lookAhead) + parseToken(lookAhead, true) + postfix();
          }
        }
        return fail();
      case 100:
        if (Match.identifier(token)) {
          token.setIdentifierCat(IdentifierCat.SUBROUTINE);
          subroutineCommand = subroutineCommand + token.getValue();
          return parseToken(token, true);
        }
        return fail();
      case 101:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 102:
        if (compileExpressionList == null)
          compileExpressionList = new CompileExpressionList(tab);
        return handleChildClass(compileExpressionList, token);
      case 103:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 104:
        return buildSubroutineCallCommand(subroutineCommand, compileExpressionList.getNumArgs()) + postfix();

      case 200:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab);
        return handleChildClass(compileExpression, token);
      case 201:
        return parseToken(token, Match.symbol(token, Symbol.BRACKET_R));
      case 202:
        return postfix();

      case 300:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab);
        return handleChildClass(compileExpression, token);
      case 301:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 302:
        return postfix();

      case 400:
        if (compileTerm == null)
          compileTerm = new CompileTerm(tab);
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
