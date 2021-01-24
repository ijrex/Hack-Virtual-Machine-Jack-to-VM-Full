package compilationEngine;

import java.io.IOException;

import token.*;

public class CompilationEngine {
  CompileClass compileClass;

  public CompilationEngine() {
    this.reset();
  }

  public void reset() {
    compileClass = new CompileClass();
  }

  public String parseToken(Token token) throws IOException {

    if (!compileClass.finished)
      return compileClass.handleToken(token);

      return "@todo throw error";
  }

  public void setClassName(String className) {
    compileClass.setClassName(className);
  }
}
