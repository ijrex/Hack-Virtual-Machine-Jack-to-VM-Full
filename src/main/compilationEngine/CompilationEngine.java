package compilationEngine;

import java.io.IOException;

import token.*;

public class CompilationEngine {
  CompileClass compileClass;

  public CompilationEngine() {
    this.reset();
  }

  public void reset() {
    compileClass = new CompileClass(0);
  }

  public String parseToken(Token token) throws IOException {

    if (!compileClass.finished)
      return compileClass.handleToken(token);

    return "...\n";
  }
}
