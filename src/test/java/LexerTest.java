import com.tinylang.Lexer;
import com.tinylang.token.Token;
import com.tinylang.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

//    @Test
//    void testEmptyInput() {
//        String source = "";
//        Lexer lexer = new Lexer(source);
//        List<Token> tokens = lexer.scanTokens();
//        assertEquals(1, tokens.size());
//        assertEquals(TokenType.EOF, tokens.get(0).type());
//    }
//
//    @Test
//    void testSingleCharacterTokens() {
//        String source = "(){}.,-+;*";
//        Lexer lexer = new Lexer(source);
//        List<Token> tokens = lexer.scanTokens();
//
//        TokenType[] expectedTypes = {
//                TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN,
//                TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE,
//                TokenType.DOT, TokenType.COMMA,
//                TokenType.MINUS, TokenType.PLUS,
//                TokenType.SEMICOLON, TokenType.STAR,
//                TokenType.EOF
//        };
//
//        assertEquals(expectedTypes.length, tokens.size());
//        for (int i = 0; i < expectedTypes.length; i++) {
//            assertEquals(expectedTypes[i], tokens.get(i).type());
//        }
//    }
//
//    @Test
//    void testNumberAndStringLiterals() {
//            String source = " \"hello world\" 45.67";
//        Lexer lexer = new Lexer(source);
//        List<Token> tokens = lexer.scanTokens();
//
//        assertEquals(TokenType.STRING, tokens.get(0).type());
//        assertEquals("hello world", tokens.get(0).literal());
//        assertEquals(TokenType.NUMBER, tokens.get(1).type());
//        assertEquals(45.67, tokens.get(1).literal());
//        assertEquals(TokenType.EOF, tokens.get(2).type());
//    }
//
//    @Test
//    void testIdentifiersAndKeywords() {
//        String source = "let x = 10; if (x > 5) { return true; }";
//        Lexer lexer = new Lexer(source);
//        List<Token> tokens = lexer.scanTokens();
//
//        TokenType[] expectedTypes = {
//                TokenType.LET, TokenType.IDENTIFIER, TokenType.EQUAL,
//                TokenType.NUMBER, TokenType.SEMICOLON,
//                TokenType.IF, TokenType.LEFT_PAREN,
//                TokenType.IDENTIFIER, TokenType.GREATER,
//                TokenType.NUMBER, TokenType.RIGHT_PAREN,
//                TokenType.LEFT_BRACE, TokenType.RETURN,
//                TokenType.TRUE, TokenType.SEMICOLON,
//                TokenType.RIGHT_BRACE, TokenType.EOF
//        };
//
//        assertEquals(expectedTypes.length, tokens.size());
//        for (int i = 0; i < expectedTypes.length; i++) {
//            assertEquals(expectedTypes[i], tokens.get(i).type());
//        }
//    }
//
//    @Test
//    void testSimpleLetStatement() {
//        String source = "let x = 42;";
//        Lexer lexer = new Lexer(source);
//        List<Token> tokens = lexer.scanTokens();
//
//        assertEquals(TokenType.LET, tokens.get(0).type());
//        assertEquals("x", tokens.get(1).lexeme());
//        assertEquals(TokenType.EQUAL, tokens.get(2).type());
//        assertEquals(42.0, tokens.get(3).literal());
//        assertEquals(TokenType.SEMICOLON, tokens.get(4).type());
//        assertEquals(TokenType.EOF, tokens.get(5).type());
//    }
//
//    @Test
//    void testValidFunctionTokens() {
//        String source = "fn add(a, b) { return a + b; }";
//        Lexer lexer = new Lexer(source);
//        List<Token> tokens = lexer.scanTokens();
//
//        Token[] expectedTokens = {
//                new Token(TokenType.FN, "fn", "fn", 1),
//                new Token(TokenType.IDENTIFIER, "add", "add", 1),
//                new Token(TokenType.LEFT_PAREN, "(", null, 1),
//                new Token(TokenType.IDENTIFIER, "a", "a", 1),
//                new Token(TokenType.COMMA, ",", null, 1),
//                new Token(TokenType.IDENTIFIER, "b", "b", 1),
//                new Token(TokenType.RIGHT_PAREN, ")", null, 1),
//                new Token(TokenType.LEFT_BRACE, "{", null, 1),
//                new Token(TokenType.RETURN, "return", "return", 1),
//                new Token(TokenType.IDENTIFIER, "a", "a", 1),
//                new Token(TokenType.PLUS, "+", null, 1),
//                new Token(TokenType.IDENTIFIER, "b", "b", 1),
//                new Token(TokenType.SEMICOLON, ";", null, 1),
//                new Token(TokenType.RIGHT_BRACE, "}", null, 1),
//                new Token(TokenType.EOF, "", null, 1)
//        };
//        assertEquals(expectedTokens.length, tokens.size());
//        for (int i = 0; i < expectedTokens.length; i++) {
//            assertEquals(expectedTokens[i].type(), tokens.get(i).type());
//            assertEquals(expectedTokens[i].lexeme(), tokens.get(i).lexeme());
//            assertEquals(expectedTokens[i].literal(), tokens.get(i).literal());
//            assertEquals(expectedTokens[i].line(), tokens.get(i).line());
//        }
//    }

}