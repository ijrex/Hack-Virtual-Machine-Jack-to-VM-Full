package compilationEngine.tokenpasser;

import token.*;
import tokenlib.*;

public class TokenPasser {

  String activeCriteria;

  public Boolean isKeyword(Token token) {
    TokenType expected = TokenType.KEYWORD;
    activeCriteria = expected.toString();
    return token.getType() == expected;
  }

  public Boolean isSymbol(Token token) {
    TokenType expected = TokenType.SYMBOL;
    activeCriteria = expected.toString();
    return token.getType() == expected;
  }

  public Boolean isIdentifier(Token token) {
    TokenType expected = TokenType.IDENTIFIER;
    activeCriteria = expected.toString();
    return token.getType() == expected;
  }

  public Boolean isStringConst(Token token) {
    TokenType expected = TokenType.STRING_CONST;
    activeCriteria = expected.toString();
    return token.getType() == expected;
  }

  public Boolean isIntConst(Token token) {
    TokenType expected = TokenType.INT_CONST;
    activeCriteria = expected.toString();
    return token.getType() == expected;
  }

  public Boolean matchKeyword(Token token, Keyword expected) {
    activeCriteria = expected.toString();
    return token.getValue() == expected.getValue();
  }

  public Boolean matchKeyword(Token token, Keyword[] expected) {
    activeCriteria = expected.toString();
    for(Keyword keyword: expected) {
      if(matchKeyword(token, keyword)) {
        return true;
      }
    }
    return false;
  }

  public Boolean matchSymbol(Token token, Symbol expected) {
    activeCriteria = expected.toString();
    return token.getValue() == expected.getValue();
  }

  public Boolean matchSymbol(Token token, Symbol[] expected) {
    activeCriteria = expected.toString();
    for(Symbol symbol: expected) {
      if(matchSymbol(token, symbol)) {
        return true;
      }
    }
    return false;
  }

  public Boolean isType(Token token) {
    Keyword[] expected = new Keyword[] { Keyword.BOOLEAN, Keyword.CHAR, Keyword.INT };
    activeCriteria = "Identifier or " + expected.toString();

    if(matchKeyword(token, expected) || isIdentifier(token)) {
      return true;
    }
    return false;
  }

  public Boolean isKeywordConst(Token token) {
    return matchKeyword(token, new Keyword[] { Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS });
  }

  public Boolean isOp(Token token) {
    Symbol[] expected = new Symbol[] { Symbol.PLUS, Symbol.MINUS, Symbol.ASTERISK, Symbol.SLASH_FWD, Symbol.AMPERSAND,
      Symbol.PIPE, Symbol.LESS_THAN, Symbol.MORE_THAN, Symbol.EQUALS };
    return matchSymbol(token, expected);
  }

  public Boolean isUnaryOp(Token token) {
    return matchSymbol(token, new Symbol[] { Symbol.MINUS, Symbol.TILDE };);
  }

  public Boolean isSubroutineDec(Token token) {
    return matchKeyword(token, new Keyword[] { Keyword.CONSTRUCTOR, Keyword.FUNCTION, Keyword.METHOD });
  }

  public Boolean isClassVarDec(Token token) {
    return matchKeyword(token, new Keyword[] { Keyword.STATIC, Keyword.FIELD});
  }

  public Boolean isVarDec(Token token) {
    return matchKeyword(token, Keyword.VAR);
  }

  public Boolean isStatementDec(Token token) {
    return matchKeyword(token, new Keyword[] { Keyword.LET, Keyword.IF, Keyword.WHILE, Keyword.DO, Keyword.RETURN });
  }

}
