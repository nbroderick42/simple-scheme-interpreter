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

/**
 *
 * @author nick
 */
public class Lexer {

    private static final int MAX_TOKEN_LENGTH = 1023;    
    private static final Set<Integer> EXTENDED_CHARS = getExtendedChars();
    
    private final BufferedReader br;    
    private Token currentToken;

    private Lexer(Path pathToFile) 
            throws IOException, SchemeInterpreterException
    {
        this.br = Files.newBufferedReader(pathToFile);       
        this.currentToken = null;
    }

    public static Lexer scanFile(Path pathToFile) 
            throws IOException, SchemeInterpreterException 
    {
        return new Lexer(pathToFile);
    }

    public Token next() throws IOException, SchemeInterpreterException {
        currentToken = read();
        return currentToken;
    }
    
    public Token peek() throws IOException, SchemeInterpreterException {
        if (currentToken == null) {
            next();
        }        
        return currentToken;
    }
    
    private Token read() throws IOException, SchemeInterpreterException {
        int nextChar;

        for (
            nextChar = br.read();
            nextChar != -1 && Character.isWhitespace(nextChar);
            nextChar = br.read()
        ) {}

        if (nextChar == -1) {
            return Token.makeEOF();
        }
        else if (nextChar == '(') {
            return Token.makeLeftParen();
        }
        else if (nextChar == ')') {
            return Token.makeRightParen();
        }
        else if (isExtendedChar(nextChar) || Character.isAlphabetic(nextChar)) {
            return makeIdentifier(nextChar);
        }
        else if (Character.isDigit(nextChar)) {
            return makeInteger(nextChar);
        }
        else if (nextChar == '"') {
            return makeString();
        }
        else if (nextChar == '\'') {
            return Token.makeQuote();
        }
        else {
            String message = String.format(
                    "Unexpected character '%s' in program", nextChar);
            throw new SchemeInterpreterException(message);
        }
    }

    private static Set<Integer> getExtendedChars() {
        Set<Integer> extendedChars = Stream.of(
                        '!', '$', '%',
                        '&', '*', ':',
                        '<', '=', '>',
                        '?', '^', '_',
                        '~', '+', '-')
                        .map(Integer::valueOf)
                        .collect(toSet());

        return Collections.unmodifiableSet(extendedChars);
    }

    private Token makeIdentifier(int firstChar)
            throws IOException, SchemeInterpreterException
    {
        String value = getCompleteTokenValue(firstChar, Lexer::isIdentifierChar);
        return Token.makeIdentifier(value);
    }

    private Token makeInteger(int firstChar)
            throws IOException, SchemeInterpreterException
    {
        String value = getCompleteTokenValue(firstChar, Lexer::isIntegerChar);
        return Token.makeInteger(value);
    }

    private Token makeString()
            throws IOException, SchemeInterpreterException
    {
        String value = getCompleteTokenValue(br.read(), Lexer::isStringChar);
        return Token.makeString(value);
    }

    private String getCompleteTokenValue(int firstChar, Predicate<Integer> isValidChar)
            throws IOException, SchemeInterpreterException 
    {
        int length = 1, nextChar = br.read();
        String value = Character.toString((char) firstChar);
        
        while (length <= MAX_TOKEN_LENGTH && isValidChar.test(nextChar)) {
            value += (char) nextChar;
            nextChar = br.read(); 
            length++;
        }

        if (length > MAX_TOKEN_LENGTH) {
            String message = String.format(
                    "Encountered token longer than %s characters", MAX_TOKEN_LENGTH);
            throw new SchemeInterpreterException(message);
        }

        return value;
    }
    
    private static boolean isNotNewlineOrEOF(int c) {
        return c != -1 && c != '\n';
    }
    
    private static boolean isExtendedChar(int c) {
        return EXTENDED_CHARS.contains(c);
    }
    
    private static boolean isStringChar(int c) {
        return isNotNewlineOrEOF(c) && c == '"';
    }
    
    private static boolean isIdentifierChar(int c) {
        return isNotNewlineOrEOF(c) && ( 
                    isExtendedChar(c) ||
                    Character.isAlphabetic(c) ||
                    Character.isDigit(c)
               );
    }
    
    private static boolean isIntegerChar(int c) {
        return isNotNewlineOrEOF(c) && Character.isDigit(c);
    }

}
