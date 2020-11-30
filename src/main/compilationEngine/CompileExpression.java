package compilationEngine;

import token.*;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import compilationEngine.util.*;

public class CompileExpression extends Compile {

  Compile compileTerm1;
  Compile compileTerm2;

  public CompileExpression(int _tab, SymbolTable _classSymbolTable) {
    super(_tab, _classSymbolTable);
    wrapperLabel = "expression";
  }

  public String handleToken(Token token) throws IOException {
    switch (pos) {
      case -1:
        return prefix(token);
      case 0:
        if (compileTerm1 == null)
          compileTerm1 = new CompileTerm(tab, classSymbolTable);
        return handleChildClass(compileTerm1, token);
      case 1:
        if (Match.op(token)) {
          compileTerm1 = null;
          return parseToken(token, true, 0);
        }
        return postfix();
      default:
        return fail();
    }
  }
}
