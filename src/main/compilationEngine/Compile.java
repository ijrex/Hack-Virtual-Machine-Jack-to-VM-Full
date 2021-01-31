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

    throw new IOException(passTokenError(activeToken));
  }

  private String passTokenError(Token token) {
    String err = "";
    err += ErrorMessage.header("TOKEN INFO");
    err += ErrorMessage.info("Token", token.getValue());
    err += ErrorMessage.info("Routine", routineLabel);
    err += ErrorMessage.info("Case", String.valueOf(pos));
    return err;
  }

  /* End Routine */

  protected String endRoutine() {
    finished = true;
    return "";
  }

  /* Fail Routine */

  protected String fail() throws IOException {
    throw new IOException("ERROR: Failed while parsing " + this.getClass());
  }


  /* Internal API to handle own routine */

  protected String handleRoutine() throws IOException {
    throw new IOException();
  }

  /* Handle Child Class */
  /* External API for parent routines */

  protected String handleChildClass(Compile compiler) throws IOException {
    if (!compiler.isComplete()) {
      String str = compiler.handleToken(activeToken);
      if (compiler.isComplete()) {
        pos++;
        return str + handleToken(activeToken);
      }
      return str;
    }
    throw new IOException(childClassError(activeToken, compiler));
  }

  public String handleToken(Token token) throws IOException {
    activeToken = token;
    return handleRoutine();
  }


  private String childClassError(Token token, Compile compiler) {
    String str = "\n";
    str += "ERROR: Cannot pass \"" + token.getValue() + "\"\n";
    str += "ERROR (Continued): Handling child class \"" + compiler.getClass() + "\"";

    return str;
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

    throw new IOException("@todo: handle unfound symbol entry - " + activeToken.getValue());
  }

  protected SymbolEntry findSymbol(Token token) throws IOException {
    return findSymbol(token, false);
  }

  protected SymbolEntry findSymbolOrPlaceholder(Token token) throws IOException {
    SymbolEntry entry = findSymbol(token, true);

    if (entry != null)
      return entry;

    return new SymbolEntry(token, new SymbolType("external") , SymbolKind.NULL, -1);
  }


  protected String passSymbolEntry(SymbolEntry symbolEntry, Boolean pass) throws IOException {
    if (pass) {
      pos++;
      return "";
    }

    throw new IOException(passSymbolEntryError(symbolEntry));
  }

  private String passSymbolEntryError(SymbolEntry symbolEntry) {
    return "@todo: Cannot pass symbol entry \"" + symbolEntry.getName() + "\", pos = " + pos;
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
}
