package compilationenginexml;

import token.*;
import tokenlib.Symbol;

import java.io.IOException;

import compilationenginexml.symboltable.SymbolEntry;
import compilationenginexml.symboltable.SymbolTable;
import compilationenginexml.util.*;

public class CompileParameterList extends Compile {

  String varType;

  public CompileParameterList(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    super(_tab, _classSymbolTable, _scopedSymbolTable);
    wrapperLabel = "parameterList";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -2:
        return postfix();
      case -1:
        if (Match.symbol(token, Symbol.PARENTHESIS_R))
          return prefix(token, -2);
        return prefix(token);
      case 0:
        if (Match.type(token)) {
          varType = token.getValue();
          return parseToken(token, true);
        }
      case 1:
        if (Match.identifier(token)) {
          SymbolEntry symbolEntry = scopedSymbolTable.add(token, varType, "ARGUMENT");
          return parseSymbolEntry(symbolEntry, true);
        }
        return fail();
      case 2:
        if (Match.symbol(token, Symbol.COMMA))
          return parseToken(token, true, 0);
        if (Match.symbol(token, Symbol.PARENTHESIS_R))
          return postfix();
      default:
        return fail();
    }
  }

}
