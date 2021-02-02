package compilationEngine;

import token.*;
import tokenlib.Keyword;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.symboltable.SymbolKind;
import compilationEngine.symboltable.SymbolTable;
import compilationEngine.symboltable.SymbolType;
import compilationEngine.tokenpasser.TokenPasser;
import errormessage.ErrorMessage;

public abstract class Compile {
  int pos = 0;
  boolean finished = false;
  String routineLabel;

  static TokenPasser passer = new TokenPasser();
  static Token activeToken;

  // Statics to be reset
  static SymbolTable classSymbolTable;
  static SymbolTable scopedSymbolTable;
  static String className;
  static String functionName;
  static Keyword functionType;
  static Token returnType;

  static int numWhileStatements = 0;
  static int numIfStatements = 0;

  // Pass tokens

  protected String passActive() throws IOException {
    return handlePassActiveToken(true, pos + 1);
  }

  protected String passActive(Boolean pass) throws IOException {
    return handlePassActiveToken(pass, pos + 1);
  }

  protected String passActive(int nextPos) throws IOException {
    return handlePassActiveToken(true, nextPos);
  }

  protected String passActive(Boolean pass, int nextPos) throws IOException {
    return handlePassActiveToken(pass, nextPos);
  }

  private String handlePassActiveToken(Boolean pass, int nextPos) throws IOException {
    if (pass) {
      pos = nextPos;
      return "";
    }

    throw new IOException(compileError("Cannot pass token"));
  }

  /* End Routine */

  protected String endRoutine() {
    finished = true;
    return "";
  }

  /* Fail Routine */

  protected String fail() throws IOException {
    throw new IOException(compileError("Subroutine failure"));
  }

  /* External API to Handle subroutine */

  public String handleToken(Token token) throws IOException {
    activeToken = token;
    return handleRoutine();
  }

  protected String handleSubroutine(Compile compiler) throws IOException {
    if (!compiler.isComplete()) {
      String str = compiler.handleToken(activeToken);
      if (compiler.isComplete()) {
        pos++;
        return str + handleToken(activeToken);
      }
      return str;
    }
    throw new IOException(compileError("Could not finish subroutine"));
  }

  /* Internal API for all classes to handle own routine */

  protected String handleRoutine() throws IOException {
    throw new IOException(compileError("No subroutine handler found"));
  }

  protected Boolean isComplete() {
    return finished;
  }

  /* Search symbol tables and add properties to identifier tokens */

  protected SymbolEntry findSymbol(Token token, Boolean optional) throws IOException {
    SymbolEntry entry = scopedSymbolTable.find(token);

    if (entry == null)
      entry = classSymbolTable.find(token);

    if (entry != null || optional) {
      return entry;
    }

    throw new IOException(symbolTableError());
  }

  protected SymbolEntry findSymbol(Token token) throws IOException {
    return findSymbol(token, false);
  }

  protected SymbolEntry findSymbolOrStub(Token token) throws IOException {
    SymbolEntry entry = findSymbol(token, true);

    if (entry != null)
      return entry;

    return new SymbolEntry(token, new SymbolType("external") , SymbolKind.STUB, -1);
  }

  /* Reset statics */

  protected void resetStaticStatements() {
    numWhileStatements = 0;
    numIfStatements = 0;
  }

  /* Compile Expression List Only */

  public int getNumArgs() {
    return -1;
  }

  /* Errors */

  private String compileError(String errorMessage) {
    String err = "";
    err += ErrorMessage.header("COMPILATION ERROR");
    err += ErrorMessage.info("Message", errorMessage);
    err += tokenInfo();
    return err;
  }

  private String tokenInfo() {
    String err = "";
    err += ErrorMessage.header("TOKEN INFO");
    err += ErrorMessage.info("Token", activeToken.getValue());
    err += ErrorMessage.info("Expected", passer.getExpected());
    err += ErrorMessage.info("Case", String.valueOf(pos));
    err += ErrorMessage.info("Routine", routineLabel);
    return err;
  }

  private String symbolTableError() {
    String err = "";
    err += ErrorMessage.header("SYMBOL TABLE");
    err += ErrorMessage.info("Message", "Could not find symbol");
    err += tokenInfo();
    err += ErrorMessage.header("CLASS SYMBOL TABLE");
    err += classSymbolTable.print();
    err += ErrorMessage.header("SCOPED SYMBOL TABLE");
    err += scopedSymbolTable.print();

    return err;
  }
}
