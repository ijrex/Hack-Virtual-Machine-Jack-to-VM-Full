package compilationEngine;

import token.*;
import tokenlib.Keyword;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.symboltable.SymbolTable;

public abstract class Compile {
  boolean development = false;
  int tab;
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

  public Compile(int _tab) {
    this.init(_tab);
  }

  private void init(int _tab) {
    tab = _tab;
    finished = false;
  }

  private String handleParseToken(Token token, Boolean pass, int nextPos, String returnVal) throws IOException {
    if (pass) {
      pos = nextPos;
      return returnVal;
    }

    throw new IOException(parseTokenError(token));
  }

  protected String parseToken(Token token, Boolean pass) throws IOException {
    return handleParseToken(token, pass, pos + 1, "");
  }

  protected String parseToken(String command, Token token, Boolean pass) throws IOException {
    return handleParseToken(token, pass, pos + 1, command);
  }

  protected String parseToken(Token token, Boolean pass, int nextPos) throws IOException {
    return handleParseToken(token, pass, nextPos, "");
  }

  protected String parseToken(String command, Token token, Boolean pass, int nextPos) throws IOException {
    return handleParseToken(token, pass, nextPos, command);
  }

  // @todo - parse symbol entry
  protected String parseSymbolEntry(SymbolEntry symbolEntry, Boolean pass) throws IOException {
    if (pass) {
      pos++;
      return "";
    }

    throw new IOException(parseSymbolEntryError(symbolEntry));
  }

  private String parseTokenError(Token token) {
    return "ERROR: Cannot parse token \"" + token.getValue() + "\", pos = " + pos;
  }

  private String parseSymbolEntryError(SymbolEntry symbolEntry) {
    return "ERROR: Cannot parse symbol entry \"" + symbolEntry.getName() + "\", pos = " + pos;
  }

  protected Boolean isComplete() {
    return finished;
  }

  protected String tabs() {
    return "\t".repeat(tab);
  }

  protected String tabs(int modifier) {
    return "\t".repeat(tab + modifier);
  }

  /* Prefix */

  protected String prefix(Token token, int newPos) throws IOException {
    pos = newPos;
    tab++;
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
    str += "ERROR: Cannot parse \"" + token.getValue() + "\"\n";
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
