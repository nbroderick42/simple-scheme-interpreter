/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.lexer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;

/**
 *
 * @author nick
 */
public abstract class Lexer {

    private static final int MAX_TOKEN_LENGTH = 1023;

    private static final String EXTENDED_CHARS = "!$%&*/:<=>?^_~+-'";
    
    public static FileLexer makeFileLexer(File inputFile) throws IOException {
        return new FileLexer(inputFile);
    }
    
    public static ReplLexer makeReplLexer() throws IOException {
        return new ReplLexer();
    }

    private static boolean isNotNewlineOrEOF(int c) {
        return c != -1 && c != '\n';
    }

    private static boolean isExtendedChar(int c) {
        return EXTENDED_CHARS.indexOf(c) != -1;
    }
    
    private static boolean isStringChar(int c) {
        return c != -1 && c != '"';
    }

    private static boolean isIdentifierChar(int c) {
        return isNotNewlineOrEOF(c)
                && (isExtendedChar(c)
                || Character.isLetter(c)
                || Character.isDigit(c));
    }

    private static boolean isIntegerChar(int c) {
        return Character.isDigit(c);
    }
    
    private static boolean isInsignificant(int c) {
        return c == ' ' || c == '\t' || c == '\f';
    }
    
    private InputStream inputStream;
        
    private int currentChar;

    private Token currentToken;
   
    public Token peek() throws IOException {
        return currentToken != null ? currentToken : nextToken();
    }
    
    public void reset() throws IOException {
        currentToken = null;
        currentChar = 0;
    }

    protected final int scanNextChar() throws IOException {
        int nextChar = inputStream.read();
        currentChar = nextChar;
        return nextChar;
    }

    public Token nextToken() throws IOException {
        /*
         * Advance the current character to the next possible token, past
         * any whitespace
         */
        while (currentChar == 0 || (currentChar != -1 && isInsignificant(currentChar))) {
            scanNextChar();
        }

        /*
         * Set the next available token depending on the character encountered
         */
        if (currentChar == -1) {
            currentToken = makeEOF();
        }
        else if (currentChar == '\n') {
            currentToken = makeNewline();
        }
        else if (currentChar == '(') {
            currentToken = makeLparen();
        }
        else if (currentChar == ')') {
            currentToken = makeRparen();
        }
        else if (currentChar == '\'') {
            currentToken = makeQuote();
        }
        else if (currentChar == '"') {
            currentToken = makeString();
        }
        else if (currentChar == '#') {
            currentToken = makeBoolean();
        }
        else if (isIntegerChar(currentChar)) {
            currentToken = makeInteger();
        }
        else if (isIdentifierChar(currentChar)) {
            currentToken = makeIdentifier();
        }
        else {
            String message = String.format("Syntax error: Unexpected character '%c' in program", (char)currentChar);
            throw new RuntimeException(message);
        }

        //System.out.format("Lexer: returning token %s\n", currentToken.getClass().getSimpleName());
        
        return currentToken;
    }

    protected Token makeIdentifier() throws IOException {
        String value = getCompleteTokenValue(Lexer::isIdentifierChar);
        return Token.makeIdentifier(value);
    }

    protected Token makeInteger() throws IOException {
        String value = getCompleteTokenValue(Lexer::isIntegerChar);
        return Token.makeInteger(value);
    }

    protected Token makeBoolean() throws IOException {
        String value = getCompleteTokenValue(Lexer::isIdentifierChar);
        return Token.makeBoolean(value);
    }

    protected Token makeString() throws IOException {
        String value = getCompleteTokenValue(Lexer::isStringChar);
        value = value.replaceAll("\"", "");

        /*
         * Advance the current character past the quotation mark that ends
         * this token.
         */
        scanNextChar();

        return Token.makeString(value);
    }

    protected Token makeQuote() throws IOException {
        scanNextChar();
        return Token.makeQuote();
    }

    protected Token makeEOF() throws IOException {
        return Token.makeEOF();
    }

    protected Token makeLparen() throws IOException {
        scanNextChar();
        return Token.makeLparen();
    }

    protected Token makeRparen() throws IOException {
        scanNextChar();
        return Token.makeRparen();
    }
    
    protected Token makeNewline() throws IOException {
        return Token.makeNewline();
    }

    protected String getCompleteTokenValue(Predicate<Integer> isValidChar) throws IOException {
        int length = 0;
        StringBuilder sb = new StringBuilder();

        do {
            sb.append((char) currentChar);
            scanNextChar();
            length++;
        } while (length <= MAX_TOKEN_LENGTH && isValidChar.test(currentChar));

        if (length > MAX_TOKEN_LENGTH) {
            String message = String.format(
                    "Syntax error: Encountered token longer than %s characters", MAX_TOKEN_LENGTH);
            throw new RuntimeException(message);
        }

        return sb.toString();
    }

    protected int getCurrentChar() {
        return currentChar;
    }
    
    protected void setCurrentChar(int currentChar) {
        this.currentChar = currentChar;
    }
    
    protected Token getCurrentToken() {
        return currentToken;
    }
    
    protected void setCurrentToken(Token currentToken) {
        this.currentToken = currentToken;
    }
    
    protected InputStream getInputStream() {
        return inputStream;
    }
    
    protected void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
}
