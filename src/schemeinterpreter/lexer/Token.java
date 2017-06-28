/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.lexer;

/**
 *
 * @author nick
 */
public class Token {

    private final TokenType type;
    private final String value;

    private Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Token makeLeftParen() {
        return new Token(TokenType.LPAREN, "(");
    }

    public static Token makeRightParen() {
        return new Token(TokenType.RPAREN, ")");
    }
    
    public static Token makeQuote() {
        return new Token(TokenType.QUOTE, "'");
    }

    public static Token makeString(String value) {
        return new Token(TokenType.STRING, value);
    }

    public static Token makeIdentifier(String value) {
        return new Token(TokenType.IDENTIFIER, value);
    }

    public static Token makeInteger(String value) {
        return new Token(TokenType.INTEGER, value);
    }

    public static Token makeEOF() {
        return new Token(TokenType.EOF, "");
    }

    public boolean isEOF() {
        return type == TokenType.EOF;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("( %s, %s )", type, value);
    }
}
