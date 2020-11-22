package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.Match;
import compilationEngine.symboltable.*;

public class CompileClassVarDec extends Compile {

  SymbolTable classSymbolTable;

  Keyword varKind;
  String varType;

  public CompileClassVarDec(int _tab, SymbolTable classSymbolTable) {
    super(_tab);
    wrapperLabel = "classVarDec";

    this.classSymbolTable = classSymbolTable;
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (Match.isClassVarDec(token)) {
          varKind = token.getKeyword();
          return parseToken(token, true);
        }
        return fail();
      case 1:
        if (Match.type(token)) {
          // @todo: Parse varType as enum
          varType = token.getValue();
          return parseToken(token, true);
        }
        return fail();
      case 2:
        if (Match.identifier(token)) {
          token.setKind(varKind.toString());
          token.setIdentifierType(varType);
          classSymbolTable.add(token);
          return parseToken(token, true);
        }
        return fail();
      case 3:
        if (Match.symbol(token, Symbol.COMMA))
          return parseToken(token, true, 2);
        if (Match.symbol(token, Symbol.SEMI_COLON))
          return parseToken(token, true, 4);
        return fail();
      case 4:
        return postfix();
      default:
        return fail();
    }
  }

}
