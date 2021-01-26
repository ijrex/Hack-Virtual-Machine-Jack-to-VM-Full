package errormessage;

public class ErrorMessage {

  private static int width = 90;
  private static String divider = "-";
  private static int colPadding = 16;

  private static String compose(String str) {
    return str + "\n";
  }

  public static String header(String title) {
    String header = title + ": ";
    return "\n" + compose(header + divider.repeat(width - header.length()));
  }
  public static String divider() {
    return compose(divider.repeat(width));
  }

  public static String info(String key, String val) {
    return compose(key + " ".repeat(colPadding - key.length()) + val);
  }
}
