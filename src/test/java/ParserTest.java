import com.tinylang.Lexer;
import com.tinylang.token.Token;
import com.tinylang.Parser;
import com.tinylang.ast.Stmt;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testParseLetStatement() {
        String source = "let x = 42;";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertInstanceOf(Stmt.Let.class, statements.getFirst());
        Stmt.Let letStmt = (Stmt.Let) statements.getFirst();
        assertEquals("x", letStmt.name);
    }
}