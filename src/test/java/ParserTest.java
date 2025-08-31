import com.tinylang.Lexer;
import com.tinylang.token.Token;
import com.tinylang.Parser;
import com.tinylang.ast.Stmt;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testParseExpressionStatement() {
        String source = "1 + 2 * 3;";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertInstanceOf(Stmt.Expression.class, statements.getFirst());
    }

    @Test
    public void testParseIfStatement() {
        String source = "if (x > 0) { print x; } else { print -x; }";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertInstanceOf(Stmt.If.class, statements.getFirst());
        Stmt.If ifStmt = (Stmt.If) statements.getFirst();
        assertNotNull(ifStmt.elseBranch);
    }

    @Test
    public void testParseWhileStatement() {
        String source = "while (x < 10) { x = x + 1; }";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertInstanceOf(Stmt.While.class, statements.getFirst());
    }

    @Test
    public void testParseReturnStatement() {
        String source = "return x + 1;";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertInstanceOf(Stmt.Return.class, statements.getFirst());
        Stmt.Return returnStmt = (Stmt.Return) statements.getFirst();
        assertNotNull(returnStmt.value);
    }

    @Test
    public void testParseBlockStatement() {
        String source = "{ let x = 10; print x; }";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertInstanceOf(Stmt.Block.class, statements.getFirst());
        Stmt.Block blockStmt = (Stmt.Block) statements.getFirst();
        assertEquals(2, blockStmt.statements.size());
    }

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