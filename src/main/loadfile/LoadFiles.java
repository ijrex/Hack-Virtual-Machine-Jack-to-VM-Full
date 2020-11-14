package loadfile;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import loadfile.util.*;

public class LoadFiles {

  private File[] files;
  private String path;

  public LoadFiles(String dir, String extention) {
    path = dir;
    this.load(dir, extention);
    this.confirmFilesFound(dir, extention);
  }

  public File[] getFiles() {
    return files;
  }

  public String getDirectoryPath() {
    return path;
  }

  private void load(String arg, String extention) {
    try {
      File sourceDir = new File(arg);

      if (sourceDir.isDirectory()) {

        FileFilter filter = new FileFilter() {
          public boolean accept(File f) {
            return Util.getFileExtension(f.getName()).matches(extention);
          }
        };

        files = sourceDir.listFiles(filter);

      } else {
        throw new FileNotFoundException();
      }
    } catch (FileNotFoundException e) {
      String error = "ERROR: Directory \"" + arg + "\" could not be found\n";
      error += "ERROR (continued): Search directory: \"" + System.getProperty("user.dir");

      System.out.println(error);
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void confirmFilesFound(String arg, String extention) {
    try {
      if (files.length <= 0) {
        throw new FileNotFoundException();
      }

    } catch (FileNotFoundException e) {
      String error = "ERROR: No \"." + extention + "\" files found\n";
      error += "ERROR (continued): Search directory: \"" + System.getProperty("user.dir") + "/" + arg;

      System.out.println(error);
      e.printStackTrace();
      System.exit(1);
    }
  }
}
