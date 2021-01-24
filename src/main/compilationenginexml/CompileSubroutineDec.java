package compilationenginexml;

import token.*;
import tokenlib.*;

import java.io.IOException;

import compilationenginexml.symboltable.SymbolTable;
import compilationenginexml.util.Match;

public class CompileSubroutineDec extends Compile {

  Compile compileParameterList;
  Compile compileSubroutineBody;

  SymbolTable scopedSymbolTable;

  public CompileSubroutineDec(int _tab, SymbolTable _classSymbolTable) {
    super(_tab, _classSymbolTable);
    wrapperLabel = "subroutineDec";

    scopedSymbolTable = new SymbolTable();
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token,
            Match.keyword(token, new Keyword[] { Keyword.CONSTRUCTOR, Keyword.FUNCTION, Keyword.METHOD }));
      case 1:
        if(Match.type(token, Keyword.VOID)) {
          if(Match.identifier(token))
            token.setIdentifierCat(IdentifierCat.CLASS);
          return parseToken(token, true);
        }
        return fail();
      case 2:
        if(Match.identifier(token)) {
          token.setIdentifierCat(IdentifierCat.SUBROUTINE_DEC);
          return parseToken(token, true);
        }
        return fail();
      case 3:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_L));
      case 4:
        if (compileParameterList == null)
          compileParameterList = new CompileParameterList(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileParameterList, token);
      case 5:
        return parseToken(token, Match.symbol(token, Symbol.PARENTHESIS_R));
      case 6:
        if (compileSubroutineBody == null)
          compileSubroutineBody = new CompileSubroutineBody(tab, classSymbolTable, scopedSymbolTable);
        return handleChildClass(compileSubroutineBody, token);
      case 7:
        return postfix();
      default:
        return fail();
    }
  }
}
