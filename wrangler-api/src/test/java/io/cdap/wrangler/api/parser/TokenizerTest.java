package io.cdap.wrangler.api.parser;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link Tokenizer} class.
 */
public class TokenizerTest {
    @Test
    public void testBasicTokenization() {
        Tokenizer tokenizer = new Tokenizer("set-column :col1 'value'");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(4, tokens.size());
        assertEquals(TokenType.TEXT, tokens.get(0).type());
        assertEquals("set-column", tokens.get(0).value());
        assertEquals(TokenType.COLUMN_NAME, tokens.get(1).type());
        assertEquals(":col1", tokens.get(1).value());
    }

    @Test
    public void testTimeDurationToken() {
        Tokenizer tokenizer = new Tokenizer("sleep 10s");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(2, tokens.size());
        assertEquals(TokenType.TIME_DURATION, tokens.get(1).type());
        assertEquals("10s", tokens.get(1).value());
    }

    @Test
    public void testByteSizeToken() {
        Tokenizer tokenizer = new Tokenizer("limit 1MB");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(2, tokens.size());
        assertEquals(TokenType.BYTE_SIZE, tokens.get(1).type());
        assertEquals("1MB", tokens.get(1).value());
    }

    @Test
    public void testNumericToken() {
        Tokenizer tokenizer = new Tokenizer("value 123 45.67");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(3, tokens.size());
        assertEquals(TokenType.NUMERIC, tokens.get(1).type());
        assertEquals("123", tokens.get(1).value());
        assertEquals(TokenType.NUMERIC, tokens.get(2).type());
        assertEquals("45.67", tokens.get(2).value());
    }

    @Test
    public void testBooleanToken() {
        Tokenizer tokenizer = new Tokenizer("flag true false");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(3, tokens.size());
        assertEquals(TokenType.BOOLEAN, tokens.get(1).type());
        assertEquals("true", tokens.get(1).value());
        assertEquals(TokenType.BOOLEAN, tokens.get(2).type());
        assertEquals("false", tokens.get(2).value());
    }

    @Test
    public void testMixedTokens() {
        Tokenizer tokenizer = new Tokenizer("process :input 10s 5MB true 3.14");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(6, tokens.size());
        assertEquals(TokenType.TEXT, tokens.get(0).type());
        assertEquals(TokenType.COLUMN_NAME, tokens.get(1).type());
        assertEquals(TokenType.TIME_DURATION, tokens.get(2).type());
        assertEquals(TokenType.BYTE_SIZE, tokens.get(3).type());
        assertEquals(TokenType.BOOLEAN, tokens.get(4).type());
        assertEquals(TokenType.NUMERIC, tokens.get(5).type());
    }

    @Test
    public void testEdgeCases() {
        // Test empty string
        Tokenizer emptyTokenizer = new Tokenizer("");
        assertTrue(emptyTokenizer.tokenize().isEmpty());

        // Test only whitespace
        Tokenizer whitespaceTokenizer = new Tokenizer("   ");
        assertTrue(whitespaceTokenizer.tokenize().isEmpty());

        // Test invalid time duration
        Tokenizer invalidTimeTokenizer = new Tokenizer("sleep 10x");
        List<Token> invalidTimeTokens = invalidTimeTokenizer.tokenize();
        assertEquals(2, invalidTimeTokens.size());
        assertEquals(TokenType.TEXT, invalidTimeTokens.get(1).type());

        // Test invalid byte size
        Tokenizer invalidSizeTokenizer = new Tokenizer("limit 10XB");
        List<Token> invalidSizeTokens = invalidSizeTokenizer.tokenize();
        assertEquals(2, invalidSizeTokens.size());
        assertEquals(TokenType.TEXT, invalidSizeTokens.get(1).type());
    }

    @Test
    public void testComplexExpressions() {
        Tokenizer tokenizer = new Tokenizer("filter exp:{ :age > 30 } prop:{ key1=value1, key2=value2 }");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(5, tokens.size());
        assertEquals(TokenType.TEXT, tokens.get(0).type());
        assertEquals(TokenType.EXPRESSION, tokens.get(1).type());
        assertEquals(TokenType.PROPERTIES, tokens.get(3).type());
    }
}
