package token;

import tokenlib.*;

public class IntConstToken extends Token {

  public IntConstToken(String value) {
    super(value);

    try {
      int intValue = Integer.parseInt(value);

      if (intValue > 32767)
        throw new NumberFormatException();

    } catch (NumberFormatException e) {
      System.out.println("ERROR: \"" + value + "\" exceeds max value of 32767");
      e.printStackTrace();
      System.exit(1);
    }

    this.type = TokenType.INT_CONST;

  }

  public String getLabel() {
    return "integerConstant";
  }
}
