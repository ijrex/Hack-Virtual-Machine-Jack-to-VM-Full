package compilationEngine;

import token.*;
import tokenlib.Keyword;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.symboltable.SymbolTable;
import compilationEngine.tokenpasser.TokenPasser;
import errormessage.ErrorMessage;

public abstract class Compile {
  int pos = 0;
  boolean finished = false;
  String wrapperLabel;

  static TokenPasser passer = new TokenPasser();
  static Token activeToken;

  // Statics to be reset
  static SymbolTable classSymbolTable;
  static SymbolTable scopedSymbolTable;
  static String className;
  static String functionName;
  static Keyword functionType;
  static Token returnType;

  static int numFieldVars = 0;
  static int numLocals = 0;

  // VM Label counts
  static int numWhileStatements = 0;
  static int numIfStatements = 0;

  public Compile() {
    this.init();
  }

  private void init() {
    finished = false;
  }

  // Pass tokens

  private String handlePassActiveToken(Boolean pass, int nextPos) throws IOException {
    if (pass) {
      pos = nextPos;
      return "";
    }

    throw new IOException(passTokenError(activeToken));
  }

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

  protected String passSymbolEntry(SymbolEntry symbolEntry, Boolean pass) throws IOException {
    if (pass) {
      pos++;
      return "";
    }

    throw new IOException(passSymbolEntryError(symbolEntry));
  }

  private String passTokenError(Token token) {
    String err = "";
    err += ErrorMessage.header("TOKEN INFO");
    err += ErrorMessage.info("Token", token.getValue());
    err += ErrorMessage.info("Routine", wrapperLabel);
    err += ErrorMessage.info("Case", String.valueOf(pos));
    return err;
  }

  private String passSymbolEntryError(SymbolEntry symbolEntry) {
    return "ERROR: Cannot pass symbol entry \"" + symbolEntry.getName() + "\", pos = " + pos;
  }

  protected Boolean isComplete() {
    return finished;
  }

  /* Postfix */

  protected String postfix() {
    finished = true;
    return "";
  }

  /* Fail */

  protected String fail() throws IOException {
    throw new IOException("ERROR: Failed while parsing " + this.getClass());
  }

  public String handleToken(Token token) throws IOException {
    activeToken = token;
    return handleRoutine();
  }

  protected String handleRoutine() throws IOException {
    throw new IOException();
  }

  /* Handle Child Class */

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

  private String childClassError(Token token, Compile compiler) {
    String str = "\n";
    str += "ERROR: Cannot pass \"" + token.getValue() + "\"\n";
    str += "ERROR (Continued): Handling child class \"" + compiler.getClass() + "\"";

    return str;
  }

  /* Search symbol tables and add properties to identifier tokens */

  protected boolean setIdentifierPropsIfSymbolExists(Token token) {
    SymbolEntry entry = scopedSymbolTable.find(token);

    if (entry == null)
      entry = classSymbolTable.find(token);

    if (entry != null) {
      token.setIdentifierCat(entry.getKindtoString());
      token.setRunningIndex(entry.getKey());
      token.setVarType(entry.getType());
      return true;
    }

    return false;
  }

  protected void handleIdentifierVarName(Token token) throws IOException {
    boolean isSet = setIdentifierPropsIfSymbolExists(token);

    if (!isSet) {
      throw new IOException("ERROR: Undefined symbol \"" + token.getValue() + "\"is not a variable.\n");
    }
  }

  protected void handleIdentifierClassOrVarName(Token token) throws IOException {
    boolean isSet = setIdentifierPropsIfSymbolExists(token);

    if (!isSet) {
      token.setIdentifierCat(IdentifierCat.CLASS);
    }
  }

  /* Reset statics */

  protected void resetStaticStatements() {
    numWhileStatements = 0;
    numIfStatements = 0;
    numFieldVars = 0;
    numLocals = 0;
  }

  /* Compile Expression List Only */

  public int getNumArgs() {
    return -1;
  }
}
