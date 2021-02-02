package compilationEngine;

import java.io.IOException;

import errormessage.ErrorMessage;
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

    throw new IOException(ErrorMessage.header("CompilationEngine Error") + ErrorMessage.info("Message", "Compilation could not be finished"));
  }

  public void setClassName(String className) {
    compileClass.setClassName(className);
  }
}
