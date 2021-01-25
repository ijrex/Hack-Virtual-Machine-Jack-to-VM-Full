package tokenizer;

import loadfile.*;
import token.*;
import tokenlib.*;
import tokenizer.util.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import compilationEngine.CompilationEngine;

import java.util.LinkedList;
import java.util.HashMap;

import java.io.FileWriter;

public class Tokenizer {

  File[] sourceFiles;
  String sourceDir;

  TokenTypeLib tokenTypeLib;

  CompilationEngine compilationEngineVM;

  HashMap<String, LinkedList<Token>> tokenizedFiles = new HashMap<String, LinkedList<Token>>();

  int activeLinePos = 0;
  String activeLine;

  public Tokenizer(LoadFiles files) {
    sourceFiles = files.getFiles();
    sourceDir = files.getDirectoryPath();

    tokenTypeLib = new TokenTypeLib();

    compilationEngineVM = new CompilationEngine();

    createTokenedFiles();
  }

  private void createTokenedFiles() {
    for (File sourceFile : sourceFiles) {
      createTokenedFile(sourceFile);
    }
  }

  private void createTokenedFile(File sourceFile) {

    try {
      Scanner fileScanner = new Scanner(sourceFile);

      String sourcePath = sourceFile.getPath();

      FileWriter fileWriter = new FileWriter(Util.getOutputFilePath(sourcePath, "vm"), false);

      compilationEngineVM.setClassName(Util.getFileName(sourceFile));

      Boolean multilineComment = false;

      while (fileScanner.hasNextLine()) {
        activeLinePos++;

        activeLine = fileScanner.nextLine().trim();

        if (activeLine.startsWith("/*"))
          multilineComment = true;

        Boolean multilineCommentEnd = false;
        if (activeLine.endsWith("*/")) {
          multilineComment = false;
          multilineCommentEnd = true;
        }

        activeLine = Util.trimExcess(activeLine, multilineComment, multilineCommentEnd);

        if (activeLine.length() > 0) {
          parseLineToTokens(activeLine, fileWriter);
        }
      }

      compilationEngineVM.reset();

      System.out.println("SUCCESS: " + sourcePath);
      fileWriter.close();
      fileScanner.close();

    } catch (IOException e) {
      System.out.println("PARSING ERROR: ---------------------------------------");
      System.out.println("File: \t" + sourceFile.getName());
      System.out.println("Pos: \t" + activeLinePos);
      System.out.println("Line: \t" + "\"" + activeLine + "\"");
      System.out.println(e.getMessage());
      System.out.println("------------------------------------------------------");
      // e.printStackTrace();
      System.exit(1);
    }
  }

  private void parseLineToTokens(String line, FileWriter fileWriterVM) throws IOException {
    String parsedLine = line;

    while (parsedLine.length() > 0) {
      Token token = matchNextToken(parsedLine);
      String value = token.getValue();

      fileWriterVM.write(compilationEngineVM.parseToken(token));

      parsedLine = parsedLine.substring(value.length()).trim();
    }
  }

  private Token matchNextToken(String line) throws IOException {
    Token token = tokenTypeLib.getTokenTypeFromString(line);

    if (token != null)
      return token;

    System.out.println("Cannot parse: " + line);
    throw new IOException();
  }

}
