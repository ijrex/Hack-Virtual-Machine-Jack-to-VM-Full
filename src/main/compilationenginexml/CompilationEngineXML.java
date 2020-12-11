package compilationenginexml;

import java.io.IOException;

import compilationenginexml.symboltable.SymbolTable;
import token.*;

public class CompilationEngineXML {
  CompileClass compileClass;

  SymbolTable classSymbolTable;

  public CompilationEngineXML() {
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
}
