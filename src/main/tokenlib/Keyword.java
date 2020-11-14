package tokenlib;

public enum Keyword {
  CLASS("class"), CONSTRUCTOR("constructor"), FUNCTION("function"), METHOD("method"), FIELD("field"), STATIC("static"),
  VAR("var"), INT("int"), CHAR("char"), BOOLEAN("boolean"), VOID("void"), TRUE("true"), FALSE("false"), NULL("null"),
  THIS("this"), LET("let"), DO("do"), IF("if"), ELSE("else"), WHILE("while"), RETURN("return"),;

  private final String value;

  private Keyword(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Keyword getType(String str) {

    for (Keyword value : Keyword.values()) {
      if (value.name().equalsIgnoreCase(str))
        return value;
    }
    return null;
  }

}
