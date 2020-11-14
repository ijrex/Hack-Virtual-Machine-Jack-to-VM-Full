package loadfile.util;

import java.util.Arrays;

public class Util {
  public static String getFileExtension(String fileName) {
    if (fileName.lastIndexOf(".") > 0)
      return fileName.substring(fileName.lastIndexOf(".") + 1);
    else
      return "";
  }

  public static Boolean argHasFileExtention(String fileName) {
    return fileName.lastIndexOf(".") > 0;
  }

  public static void checkCorrectExtension(String arg, String[] extentions) {
    try {
      String argFileExtention = getFileExtension(arg);

      if (!Arrays.asList(extentions).contains(argFileExtention)) {
        throw new IllegalArgumentException();
      }

    } catch (IllegalArgumentException e) {
      System.out.println("ERROR: File types \"" + Arrays.toString(extentions) + "\" required");
      e.printStackTrace();
      System.exit(1);
    }
  }

}