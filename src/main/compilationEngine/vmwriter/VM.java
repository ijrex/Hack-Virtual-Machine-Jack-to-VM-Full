package compilationEngine.vmwriter;

public class VM {
  public static String writeFunction(String name, int nLocals) {
    return "function " + name + " " + nLocals + "\n";
  }
}
