package com;

import com.lexer.Lexer;
import com.lexer.Token;
import com.parser.Parser;
import com.util.TLangFileValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.err.println("Invalid number of arguments, please enter the path to a .tl file");
            return;
        }

        Path path = getPath(args[0]);
        if (path == null || !TLangFileValidator.isValidTLangFile(path)) {
            printError("Invalid path: " + args[0]);
            return;
        }

        String content = readFile(path);
        if (content == null) return;
        Lexer lexer = new Lexer(content);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        parser.parse();
    }

    private static void printError(String s) {
        System.err.println(s);
    }

    private static Path getPath(String arg) {
        try {
            return Paths.get(arg);
        } catch (InvalidPathException ipe) {
            return null;
        }
    }

    private static String readFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            printError("An error has occurred while reading the file: " + path);
            return null;
        }
    }
}
