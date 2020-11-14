package tokenlib;

public enum Symbol {
  BRACE_L("{"), BRACE_R("}"), PARENTHESIS_L("("), PARENTHESIS_R(")"), BRACKET_L("["), BRACKET_R("]"), COMMA(","),
  PERIOD("."), SEMI_COLON(";"), PLUS("+"), MINUS("-"), ASTERISK("*"), SLASH_FWD("/"), AMPERSAND("&"), PIPE("|"),
  LESS_THAN("<"), MORE_THAN(">"), EQUALS("="), UNDERSCORE("_"), TILDE("~");

  private final String value;

  private Symbol(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Symbol getType(String str) {

    for (Symbol value : Symbol.values()) {
      if (value.name().equalsIgnoreCase(str))
        return value;
    }
    return null;
  }

}
