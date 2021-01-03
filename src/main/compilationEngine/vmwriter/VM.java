package compilationEngine.vmwriter;

import tokenlib.Symbol;

public class VM {
  public static String writeFunction(String name, int nLocals) {
    return "function " + name + " " + nLocals + "\n";
  }

  public static String writePush(String segment, int index) {
    return "push " + segment + " " + index + "\n";
  }

  public static String writePop(String segment, int index) {
    return "pop " + segment + " " + index + "\n";
  }

  public static String writeCall(String name, int nArgs) {
    return "call " + name + " " + nArgs + "\n";
  }

  public static String writeArithmetic(Symbol symbol) {
    String command = "";

    switch (symbol) {
      case PLUS:
        command = "add";
        break;
      case MINUS:
        command = "sub";
        break;
      case MORE_THAN:
        command = "gt";
        break;
      case LESS_THAN:
        command = "lt";
        break;
      case AMPERSAND:
        command = "and";
        break;
      case EQUALS:
        command = "eq";
        break;
      default:
        break;
    }

    return command + "\n";
  }

  public static String writeUnaryOp(Symbol symbol) {
    String command = "";

    switch (symbol) {
      case MINUS:
        command = "neg";
        break;
      case TILDE:
        command = "not";
        break;
      default:
        break;
    }

    return command + "\n";
  }

  public static String writeReturn() {
    return "return\n";
  }
}
