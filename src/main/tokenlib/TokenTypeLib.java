package tokenlib;

import java.util.regex.Pattern;

import token.*;
import tokenlib.util.*;

import java.util.regex.Matcher;

import java.util.Map;
import java.util.HashMap;

public class TokenTypeLib {

  Pattern keywordPat, symbolPat, intConstPat, stringConstPat, identifierPat;

  Map<String, Token> tokens = new HashMap<String, Token>();

  public TokenTypeLib() {
    keywordPat = Pattern.compile(Util.getKeywordReg());
    symbolPat = Pattern.compile(Util.getSymbolReg());
    intConstPat = Pattern.compile("^\\d+");
    stringConstPat = Pattern.compile("^\".*\"");
    identifierPat = Pattern.compile("^[A-Za-z][\\w]*");

    for (Keyword keyword : Keyword.values()) {
      String value = keyword.getValue();
      tokens.put(value, new KeywordToken(value, keyword));
    }

    for (Symbol symbol : Symbol.values()) {
      String value = symbol.getValue();
      tokens.put(value, new SymbolToken(value, symbol));
    }
  }

  public Token getTokenTypeFromString(String line) {

    Matcher mat;

    mat = keywordPat.matcher(line);
    if (mat.find())
      return tokens.get(mat.group(0).trim());

    mat = symbolPat.matcher(line);
    if (mat.find())
      return tokens.get(mat.group(0));

    mat = intConstPat.matcher(line);
    if (mat.find()) {
      String token = mat.group(0);
      return putNewToken(token, new IntConstToken(token));
    }

    mat = stringConstPat.matcher(line);
    if (mat.find()) {
      String token = mat.group(0);
      return putNewToken(token, new StrConstToken(token));
    }

    mat = identifierPat.matcher(line);
    if (mat.find()) {
      String token = mat.group(0);
      return putNewToken(token, new IdentifierToken(token));
    }

    return null;
  }

  private Token putNewToken(String val, Token newToken) {
    if (tokens.get(val) == null) {
      tokens.put(val, newToken);
    }
    return tokens.get(val);
  }
}
