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

  // Table printing

  private static String[] parseData(String data) {
    String[] dataArr = data.split(",");

    for(int i = 0; i < dataArr.length; i++) {
      dataArr[i] = dataArr[i].trim();
    }

    return dataArr;
  }

  private static int[] calculateColWidths(String[] headers, String[] data) {
    int[] colWidth = new int[headers.length];

    for(int i = 0; i < headers.length; i++) {
      colWidth[i] = headers[i].length();
    }

    for(int i = 0; i < data.length; i++) {
      int col = i%headers.length;
      String val = data[i];
      int width = colWidth[col];
      colWidth[col] = val.length() > width ? val.length() : width;
    }

    return colWidth;
  }

  private static String paddedColumn(int colWidth, String str, int tabs) {
    return str + " ".repeat(colWidth - str.length()) + "\t".repeat(tabs);
  }

  private static String formatRow(String[] cols, int[] colWidth, int tabs) {
    String output = "";

    for(int i=0; i < cols.length; i++) {
      output += paddedColumn(colWidth[i], cols[i], tabs);
    }

    return output + "\n";
  }

  private static String formatData(String[] data, int[] colWidth, int tabs) {
    String output = "";

    for(int i = 0; i < data.length; i++) {

      String val = data[i];
      int col = i%colWidth.length;

      output += paddedColumn(colWidth[col], val, tabs);

      if((i + 1) % colWidth.length == 0) {
        output += "\n";
      }
    }

    return output;
  }

  public static String printTable(String[] headers, String data) {

    String output = "\n";
    int tabs = 2;

    String[] dataArr = parseData(data);
    int[] colWidth = calculateColWidths(headers, dataArr);

    output += formatRow(headers, colWidth, tabs);
    output += formatData(dataArr, colWidth, tabs);

    return output;
  }
}
