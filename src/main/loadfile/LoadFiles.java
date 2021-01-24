package loadfile;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import loadfile.util.*;

import java.util.ArrayList;

public class LoadFiles {

  private ArrayList<File> files = new ArrayList<File>();
  private String path;

  public LoadFiles(String dir, String extention, Boolean recursive) {
    path = dir;

    if(recursive) {
      this.loadRecursive(dir, extention);
    } else {
      this.load(dir, extention);
    }

    this.confirmFilesFound(dir, extention);
  }

  public File[] getFiles() {
    return files.toArray(new File[files.size()]);
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

        for(File file: sourceDir.listFiles(filter)) {
          files.add(file);
        }

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

  private void loadRecursive(String arg, String extention) {

    File sourceDir = new File(arg);
    File[] sourceDirFiles = sourceDir.listFiles();

    for(File file: sourceDirFiles) {
      if(file.isDirectory()) {
        String path = file.getPath();
        this.load(path, extention);
        this.loadRecursive(path, extention);
      }
    }
  }

  private void confirmFilesFound(String arg, String extention) {
    try {
      if (files.size() <= 0) {
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
