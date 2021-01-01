package compilationEngine.vmwriter;

public class VM {
  public static String writeFunction(String name, int nLocals) {
    return "function " + name + " " + nLocals + "\n";
  }

  public static String writePush(String segment, int index) {
    return "push " + segment + " " + index + "\n";
  }
}
