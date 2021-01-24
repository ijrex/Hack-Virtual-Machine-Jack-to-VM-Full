package compilationEngine;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.util.Match;
import compilationEngine.symboltable.*;

public class CompileClassVarDec extends Compile {

  String varKind;
  String varType;

  public CompileClassVarDec(int _tab) {
    super(_tab);
    wrapperLabel = "classVarDec";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (Match.isClassVarDec(token)) {
          varKind = token.getKeyword().toString();
          return passToken(token, true);
        }
        return fail();
      case 1:
        if (Match.type(token)) {
          if (Match.identifier(token))
            token.setIdentifierCat(IdentifierCat.SYMBOL_DEC);
          varType = token.getValue();
          return passToken(token, true);
        }
        return fail();
      case 2:
        if (Match.identifier(token)) {
          // @todo: Check for duplicate and throw error
          SymbolEntry symbolEntry = classSymbolTable.add(token, varType, varKind);
          if(symbolEntry.getKind() == SymbolKind.FIELD) {
            numFieldVars++;
          }
          return passSymbolEntry(symbolEntry, true);
        }
        return fail();
      case 3:
        if (Match.symbol(token, Symbol.COMMA))
          return passToken(token, true, 2);
        if (Match.symbol(token, Symbol.SEMI_COLON))
          return passToken(token, true, 4);
        return fail();
      case 4:
        return postfix();
      default:
        return fail();
    }
  }

}
