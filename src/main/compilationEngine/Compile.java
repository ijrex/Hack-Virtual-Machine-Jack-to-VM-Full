package compilationEngine;

import token.*;

import java.io.IOException;

import compilationEngine.symboltable.SymbolEntry;
import compilationEngine.symboltable.SymbolTable;

public abstract class Compile {
  static String className;

  boolean development = false;
  int tab;
  int pos = -1;
  boolean finished = false;
  String wrapperLabel;
  SymbolTable classSymbolTable;
  SymbolTable scopedSymbolTable;

  // Expression counters for VM printing
  static int whileExpressionCount = 0;

  public Compile(int _tab, SymbolTable _classSymbolTable) {
    this.init(_tab, _classSymbolTable);
  }
  
  public Compile(int _tab, SymbolTable _classSymbolTable, SymbolTable _scopedSymbolTable) {
    this.init(_tab, _classSymbolTable);
    scopedSymbolTable = _scopedSymbolTable;
  }

  private void init(int _tab, SymbolTable _classSymbolTable) {
    tab = _tab;
    finished = false;
    classSymbolTable = _classSymbolTable;
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

    if(entry == null) 
      entry = classSymbolTable.find(token);

    if(entry != null) {
      token.setIdentifierCat(entry.getKindtoString());
      token.setRunningIndex(entry.getKey());
      return true;
    }

    return false;
  }

  protected Token handleIdentifierVarName(Token token) throws IOException {
    boolean isSet = setIdentifierPropsIfSymbolExists(token);

    if(!isSet) {
      throw new IOException("ERROR: Undefined symbol \"" + token.getValue() + "\"is not a variable.\n");
    }

    return token;
  }

  protected void handleIdentifierClassOrVarName(Token token) throws IOException {
    boolean isSet = setIdentifierPropsIfSymbolExists(token);

    if(!isSet) {
      token.setIdentifierCat(IdentifierCat.CLASS);            
    }
  }

  /* Compile Expression List Only */

  public int getNumArgs() {
    return -1;
  }

}