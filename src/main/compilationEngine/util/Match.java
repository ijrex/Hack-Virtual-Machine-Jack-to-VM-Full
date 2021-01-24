package compilationEngine.util;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;
import tokenlib.TokenType;

public class Match {
  public static Boolean keyword(Token token) {
    return token.getType() == TokenType.KEYWORD;
  }

  public static Boolean keyword(Token token, Keyword expected) {
    return token.getValue() == expected.getValue();
  }

  public static Boolean keyword(Token token, Keyword[] expected) {
    for (Keyword keyword : expected) {
      if (keyword(token, keyword))
        return true;
    }
    return false;
  }

  public static Boolean symbol(Token token, Symbol expected) {
    return token.getValue() == expected.getValue();
  }

  public static Boolean symbol(Token token, Symbol[] expected) {
    for (Symbol keyword : expected) {
      if (symbol(token, keyword)) {
        return true;
      }
    }
    return false;
  }

  public static Boolean identifier(Token token) {
    return token.getType() == TokenType.IDENTIFIER;
  }

  public static Boolean stringConst(Token token) {
    return token.getType() == TokenType.STRING_CONST;
  }

  public static Boolean intConst(Token token) {
    return token.getType() == TokenType.INT_CONST;
  }

  public static Boolean type(Token token) {
    Keyword[] expected = new Keyword[] { Keyword.BOOLEAN, Keyword.CHAR, Keyword.INT };
    if (keyword(token, expected)) {
      return true;
    }
    if (identifier(token)) {
      return true;
    }
    return false;
  }

  public static Boolean type(Token token, Keyword key) {
    if (type(token) || keyword(token, key))
      return true;
    return false;
  }

  public static Boolean keywordConst(Token token) {
    Keyword[] expected = new Keyword[] { Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS };

    return keyword(token, expected);
  }

  public static Boolean op(Token token) {
    Symbol[] expected = new Symbol[] { Symbol.PLUS, Symbol.MINUS, Symbol.ASTERISK, Symbol.SLASH_FWD, Symbol.AMPERSAND,
        Symbol.PIPE, Symbol.LESS_THAN, Symbol.MORE_THAN, Symbol.EQUALS };

    return symbol(token, expected);
  }

  public static Boolean unaryOp(Token token) {
    Symbol[] expected = new Symbol[] { Symbol.MINUS, Symbol.TILDE };

    return symbol(token, expected);
  }

  public static Boolean isSubroutineDec(Token token) {
    Keyword[] expected = new Keyword[] { Keyword.CONSTRUCTOR, Keyword.FUNCTION, Keyword.METHOD };

    return keyword(token, expected);
  }

  public static Boolean isClassVarDec(Token token) {
    Keyword[] expected = new Keyword[] { Keyword.STATIC, Keyword.FIELD };

    return keyword(token, expected);
  }

  public static Boolean isVarDec(Token token) {
    return keyword(token, Keyword.VAR);
  }

  public static Boolean isStatementDec(Token token) {
    Keyword[] expected = new Keyword[] { Keyword.LET, Keyword.IF, Keyword.WHILE, Keyword.DO, Keyword.RETURN };

    return keyword(token, expected);
  }
}
