package com.tinylang;

import com.tinylang.ast.Stmt;
import com.tinylang.token.Token;
import com.tinylang.token.TokenType;
import com.tinylang.printer.AstPrinter;
import com.tinylang.util.TLangFileValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TinyLang {

    private static final Interpreter interpreter = new Interpreter();
    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length > 1) {
            System.err.println("Usage: tlang <source-file>");
            return;
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String filePath) {
        Path path = getPath(filePath);
        if (path == null || !TLangFileValidator.isValidTLangFile(path)) {
            System.err.println("Invalid path: " + filePath);
            return;
        }

        String content = readFile(path);
        Lexer lexer = new Lexer(content);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        if (hadError) return;


        AstPrinter.print(statements);
    }

    private static void runPrompt() {
        return;
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
            System.err.println("An error has occurred while reading the file: " + path);
            return null;
        }
    }

    public static void error(int line, String message) {
        report(line, "", message);
    }

    public static void error(Token token, String message) {
        if (token.type() == TokenType.EOF) {
            report(token.line(), " at end", message);
        } else {
            report(token.line(), " at '" + token.lexeme() + "'", message);
        }
    }

    public static void runtimeError(String message) {
        System.err.println("Runtime Error: " + message);
        hadError = true;
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
