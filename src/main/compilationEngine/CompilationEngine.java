package compilationEngine;

import java.io.IOException;

import compilationEngine.symboltable.SymbolTable;
import token.*;

public class CompilationEngine {
  CompileClass compileClass;

  SymbolTable classSymbolTable;

  public CompilationEngine() {
    this.reset();
  }

  public void reset() {
    classSymbolTable = new SymbolTable();
    compileClass = new CompileClass(0, classSymbolTable);
  }

  public String parseToken(Token token) throws IOException {

    if (!compileClass.finished)
      return compileClass.handleToken(token);

    return "...\n";
  }

  public void setClassName(String className) {
    compileClass.setClassName(className);
  }

}
