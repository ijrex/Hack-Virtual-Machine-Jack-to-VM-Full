package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.Match;

public class CompileVarDec extends Compile {
  String varType;

  public CompileVarDec(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "varDec";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.VAR));
      case 1:
        if (Match.type(token)) {
          if(Match.identifier(token))
            token.setIdentifierCat(IdentifierCat.SYMBOL_DEC);
          varType = token.getValue();
          return parseToken(token, true);
        }
        return fail();
      case 2:
        if (Match.identifier(token)) {
          SymbolEntry symbolEntry = scopedSymbolTable.add(token, varType, "VAR");
          return parseSymbolEntry(symbolEntry, true);
        }
        return fail();
      case 3:
        if (Match.symbol(token, Symbol.COMMA))
          return parseToken(token, true, 2);
        if (Match.symbol(token, Symbol.SEMI_COLON))
          return parseToken(token, true);
        return fail();
      case 4:
        return postfix();
      default:
        return fail();
    }
  }

}
