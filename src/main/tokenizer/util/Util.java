package tokenizer.util;

public class Util {
  public static String getOutputFilePath(String pathName, String suffix) {
    String file = pathName.substring(pathName.lastIndexOf("/") + 1);
    String fileName = file.substring(0, file.lastIndexOf("."));
    return pathName.replace(file, fileName + "." + suffix);
  }

  public static String trimExcess(String str, Boolean multilineComment, Boolean multilineCommentEnd) {
    if (multilineComment || multilineCommentEnd)
      str = "";

    if (str.length() > 0) {
      int lineComment = str.indexOf("//");
      if (lineComment >= 0) {
        str = str.substring(0, lineComment);
      }
    }

    str = str.trim();
    return str;
  }
}