package compilationEngine;

import token.*;
import tokenlib.Keyword;
import tokenlib.Symbol;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileClass extends Compile {

  Compile compileClassVarDec;
  Compile compileSubroutineDec;

  public CompileClass(int _tab) {
    super(_tab);
    wrapperLabel = "class";

    classSymbolTable = new SymbolTable();
  }

  public void setClassName(String _className) {
    className = _className;
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        return parseToken(token, Match.keyword(token, Keyword.CLASS));
      case 1:
        if (Match.identifier(token)) {
          token.setIdentifierCat(IdentifierCat.CLASS_DEC);
          return parseToken(token, true);
        }
        return fail();
      case 2:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_L));
      case 3:
        if (Match.isClassVarDec(token) && compileClassVarDec == null)
          compileClassVarDec = new CompileClassVarDec(tab);
        if (compileClassVarDec != null)
          return handleChildClass(compileClassVarDec, token);
        pos++;
      case 4:
        if (Match.isClassVarDec(token) && compileClassVarDec != null) {
          compileClassVarDec = null;
          pos--;
          return handleToken(token);
        }
        pos++;
      case 5:
        if (Match.isSubroutineDec(token) && compileSubroutineDec == null)
          compileSubroutineDec = new CompileSubroutineDec(tab);
        if (compileSubroutineDec != null)
          return handleChildClass(compileSubroutineDec, token);
        pos++;
      case 6:
        if (Match.isSubroutineDec(token) && compileSubroutineDec != null) {
          compileSubroutineDec = null;
          pos--;
          return handleToken(token);
        }
        pos++;
      case 7:
        return parseToken(token, Match.symbol(token, Symbol.BRACE_R)) + postfix();
      default:
        return fail();
    }
  }
}
