/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;
import schemeinterpreter.SchemeInterpreterException;
import static schemeinterpreter.lexer.Token.makeQuote;


/**
 *
 * @author nick
 */
public class Lexer {
    
    private static final int MAX_TOKEN_LENGTH = 1023;    
    private static final String EXTENDED_CHARS = "!$%&*:<=>?^_~+-'";
    
    private int currChar;
    private final BufferedReader br;    
    private Token currentToken;
    
    private Lexer(Path pathToFile) 
            throws IOException, SchemeInterpreterException
    {
        this.br = Files.newBufferedReader(pathToFile);       
        this.currentToken = null;
        
        scanNextChar();
    }

    public static Lexer scanFile(Path pathToFile) 
            throws IOException, SchemeInterpreterException 
    {
        return new Lexer(pathToFile);
    }

    public Token next() throws IOException, SchemeInterpreterException {
        currentToken = readNextToken();
        return currentToken;
    }
    
    public Token peek() throws IOException, SchemeInterpreterException {
        if (currentToken == null) {
            next();
        }        
        return currentToken;
    }
    
    private int scanNextChar() throws IOException {
        currChar = br.read();
        return currChar;
    }
    
    private Token readNextToken() throws IOException, SchemeInterpreterException {        
        while (currChar != -1 && Character.isWhitespace(currChar)) {
            scanNextChar();
        }
        
        if (currChar == -1) {
            return makeEOF();
        }
        else if (currChar == '(') {
            return makeLparen();
        }
        else if (currChar == ')') {
            return makeRparen();
        }
        else if (currChar == '\'') {
            return makeQuote();
        }
        else if (currChar == '"') {
            return makeString();
        }
        else if (isIdentifierChar(currChar)) {
            return makeIdentifier();
        }
        else if (isIntegerChar(currChar)) {
            return makeInteger();
        }
        else {
            String message = String.format(
                    "Syntax error: Unexpected character '%s' in program", currChar);
            throw new SchemeInterpreterException(message);
        }
    }

    private Token makeQuote() 
            throws IOException, SchemeInterpreterException
    {
        scanNextChar();
        return Token.makeQuote();
    }
    
    private Token makeIdentifier()
            throws IOException, SchemeInterpreterException
    {
        String value = getCompleteTokenValue(Lexer::isIdentifierChar);
        return Token.makeIdentifier(value);
    }

    private Token makeInteger()
            throws IOException, SchemeInterpreterException
    {
        String value = getCompleteTokenValue(Lexer::isIntegerChar);
        return Token.makeInteger(value);
    }

    private Token makeString()
            throws IOException, SchemeInterpreterException
    {
        String value = getCompleteTokenValue(Lexer::isStringChar);
        value = value.replaceAll("\"", "");
        
        return Token.makeString(value);
    }
    
    
    private Token makeEOF() throws IOException {
        scanNextChar();
        return Token.makeEOF();
    }

    private Token makeLparen() throws IOException {
        scanNextChar();
        return Token.makeLparen();
    }

    private Token makeRparen() throws IOException {
        scanNextChar();
        return Token.makeRparen();
    }

    private String getCompleteTokenValue(Predicate<Integer> isValidChar)
            throws IOException, SchemeInterpreterException 
    {
        int length = 0;
        String value = "";
        
        do {
            value += (char) currChar;
            scanNextChar();
            length++;
        }
        while (length <= MAX_TOKEN_LENGTH && isValidChar.test(currChar));

        if (length > MAX_TOKEN_LENGTH) {
            String message = String.format(
                    "Syntax error: Encountered token longer than %s characters", MAX_TOKEN_LENGTH);
            throw new SchemeInterpreterException(message);
        }

        return value;
    }
    
    private static boolean isNotNewlineOrEOF(int c) {
        return c != -1 && c != '\n';
    }
    
    private static boolean isExtendedChar(int c) {
        return EXTENDED_CHARS.indexOf(c) != -1;
    }
    
    private static boolean isStringChar(int c) {
        return isNotNewlineOrEOF(c) && c == '"';
    }
    
    private static boolean isIdentifierChar(int c) {        
        return isNotNewlineOrEOF(c) 
                && (isExtendedChar(c) ||
                    Character.isLetter(c) ||
                    Character.isDigit(c));
    }
    
    private static boolean isIntegerChar(int c) {
        return isNotNewlineOrEOF(c) && Character.isDigit(c);
    }

}
