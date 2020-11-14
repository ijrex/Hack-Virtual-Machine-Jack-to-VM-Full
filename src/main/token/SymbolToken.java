package token;

import tokenlib.*;

public class SymbolToken extends Token {

  Symbol symbol;

  public SymbolToken(String value, Symbol _symbol) {
    super(value);
    this.type = TokenType.SYMBOL;
    this.symbol = _symbol;
  }

  public String getLabel() {
    return "symbol";
  }

  public Symbol getSymbol() {
    return symbol;
  }
}
