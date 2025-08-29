import com.tinylang.Lexer;
import com.tinylang.token.Token;
import com.tinylang.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void testSimpleLetStatement() {
        String source = "let x = 42;";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        assertEquals(TokenType.LET, tokens.get(0).type());
        assertEquals("x", tokens.get(1).lexeme());
        assertEquals(TokenType.EQUAL, tokens.get(2).type());
        assertEquals(42.0, tokens.get(3).literal());
        assertEquals(TokenType.SEMICOLON, tokens.get(4).type());
        assertEquals(TokenType.EOF, tokens.get(5).type());
    }
}