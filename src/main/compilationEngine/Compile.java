package compilationEngine;

import token.*;
import tokenlib.Keyword;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.symboltable.SymbolTable;

public abstract class Compile {
  boolean development = false;
  int pos = -1;
  boolean finished = false;
  String wrapperLabel;

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

  private String handlePassToken(Token token, Boolean pass, int nextPos, String returnVal) throws IOException {
    if (pass) {
      pos = nextPos;
      return returnVal;
    }

    throw new IOException(passTokenError(token));
  }

  protected String passToken(Token token, Boolean pass) throws IOException {
    return handlePassToken(token, pass, pos + 1, "");
  }

  protected String passToken(String command, Token token, Boolean pass) throws IOException {
    return handlePassToken(token, pass, pos + 1, command);
  }

  protected String passToken(Token token, Boolean pass, int nextPos) throws IOException {
    return handlePassToken(token, pass, nextPos, "");
  }

  protected String passToken(String command, Token token, Boolean pass, int nextPos) throws IOException {
    return handlePassToken(token, pass, nextPos, command);
  }

  protected String passSymbolEntry(SymbolEntry symbolEntry, Boolean pass) throws IOException {
    if (pass) {
      pos++;
      return "";
    }

    throw new IOException(passSymbolEntryError(symbolEntry));
  }

  private String passTokenError(Token token) {
    return "ERROR: Cannot pass token \"" + token.getValue() + "\", pos = " + pos;
  }

  private String passSymbolEntryError(SymbolEntry symbolEntry) {
    return "ERROR: Cannot pass symbol entry \"" + symbolEntry.getName() + "\", pos = " + pos;
  }

  protected Boolean isComplete() {
    return finished;
  }

  /* Prefix */

  protected String prefix(Token token, int newPos) throws IOException {
    pos = newPos;
    return "" + handleToken(token);
  }

  protected String prefix(Token token) throws IOException {
    return prefix(token, 0);
  }

  /* Postfix */

  /*
   * Subclass routines close out naturally on `exit` in development mode, this is
   * flagged in the XML output with a trailing `.`
   */

  protected String postfix(Boolean fakeClosure) {
    finished = true;
    return "";
  }

  protected String postfix() {
    return postfix(false);
  }

  /* Fail */

  protected String fail() throws IOException {
    if (development) {
      return postfix(true);
    } else {
      throw new IOException("ERROR: Failed while parsing " + this.getClass());
    }
  }

  protected String handleToken(Token token) throws IOException {
    throw new IOException();
  }

  /* Handle Child Class */

  protected String handleChildClass(Compile compiler, Token token) throws IOException {
    if (!compiler.isComplete()) {
      String str = compiler.handleToken(token);
      if (compiler.isComplete()) {
        pos++;
        return str + handleToken(token);
      }
      return str;
    }
    throw new IOException(childClassError(token, compiler));
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
