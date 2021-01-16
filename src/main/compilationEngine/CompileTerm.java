package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.*;
import compilationEngine.vmwriter.VM;

public class CompileTerm extends Compile {

  Compile compileExpressionList;
  Compile compileExpression;
  Compile compileTerm;

  Token lookAhead;
  Token subroutineToken;

  public CompileTerm(int _tab) {
    super(_tab);
    wrapperLabel = "term";
  }

  private String identifierTokenCommand(Token token) {
    String location = VM.parseLocation(token.getIdentifierCat());
    return VM.writePush(location, token.getRunningIndex());
  }

  private String keywordTokenCommand(Keyword keyword) {
    switch (keyword) {
      case TRUE:
        String command = VM.writePush("constant", 0);
        command += "not\n";
        return command;
      case FALSE:
        return VM.writePush("constant", 0);
      case THIS:
        return VM.writePush("pointer", 0);
      default:
        return null + "/n";
    }
  }

  private String arrayReferenceCommand() {
    String location = VM.parseLocation(lookAhead.getIdentifierCat());
    String command = VM.writePush(location, lookAhead.getRunningIndex());
    command += "add\n";
    command += VM.writePop("pointer", 1);
    command += VM.writePush("that", 0);
    return command;
  }

  private String subroutineCallCommand(int nArgs) {
    String functionCall = VM.createSubroutineName(lookAhead.getValue(), subroutineToken.getValue());
    return VM.writeCall(functionCall, nArgs);
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (Match.intConst(token))
          return VM.writePush("constant", token.getIntValue()) + parseToken(token, true, 500);
        if (Match.keywordConst(token))
          return keywordTokenCommand(token.getKeyword()) + parseToken(token, true, 500);
        if (Match.stringConst(token))
          return VM.writePush("@todo" + token.getType(), -1) + parseToken(token, true, 500);
        if (Match.identifier(token)) {
          lookAhead = token;
          pos++;
          return "";
        }
        if (Match.symbol(token, Symbol.PARENTHESIS_L))
          return parseToken(token, true, 300);

        if (Match.unaryOp(token)) {
          lookAhead = token;
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
              return identifierTokenCommand(lookAhead) + parseToken(lookAhead, true) + postfix();
          }
        }
        return fail();
      case 100:
        if (Match.identifier(token)) {
          token.setIdentifierCat(IdentifierCat.SUBROUTINE);
          subroutineToken = token;
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
        int nArgs = compileExpressionList.getNumArgs();
        return subroutineCallCommand(nArgs) + postfix();

      case 200:
        if (compileExpression == null)
          compileExpression = new CompileExpression(tab);
        return handleChildClass(compileExpression, token);
      case 201:
        return arrayReferenceCommand() + parseToken(token, Match.symbol(token, Symbol.BRACKET_R));
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
        return VM.writeUnaryOp(lookAhead.getSymbol()) + postfix();

      case 500:
        return postfix();

      default:
        return fail();
    }
  }
}
