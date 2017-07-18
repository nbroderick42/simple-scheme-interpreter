package schemeinterpreter.lexer;

import schemeinterpreter.lexer.token.Token;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Character.isDigit;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.Character.isWhitespace;
import static java.lang.String.format;
import java.nio.file.Files;
import static java.nio.file.Files.newBufferedReader;
import java.nio.file.Path;
import java.util.function.Predicate;

/**
 * The main class used for lexical analysis of a Scheme program.
 *
 * @author nick
 */
public class Lexer {

    /**
     * A token in this implementation cannot exceed 1023 characters.
     */
    private static final int MAX_TOKEN_LENGTH = 1023;

    /**
     * The set of valid starting characters (in addition to letters and digits)
     * that may begin an identifier.
     */
    private static final String EXTENDED_CHARS = "!$%&*:<=>?^_~+-'";

    /**
     * The reader for the input file.
     */
    private final BufferedReader br;

    /**
     * The current character being analyzed by the lexer.
     */
    private int currChar;

    /**
     * The most recently produced Token.
     */
    private Token currentToken;

    /**
     * Creates a new Lexer using the provided Path
     *
     * @param pathToFile The path to the file to analyze
     */
    private Lexer(Path pathToFile) throws IOException {
        this(newBufferedReader(pathToFile));
    }

    private Lexer(BufferedReader br) throws IOException {
        this.br = br;
        this.currentToken = null;

        scanNextChar();
    }

    /**
     * Static factory method that returns a lexer that analyzes
     * the file at the given path.
     *
     * @param pathToFile The path to the file to analyze
     *
     * @return A Lexer which analyzes the given file
     *
     * @throws java.io.IOException
     */
    public static Lexer scanFile(Path pathToFile) throws IOException {
        return new Lexer(pathToFile);
    }

    /**
     * Advances the lexer and returns the next available token from
     * the input file. Will return an instance of Token.EOF if invoked when
     * no more tokens are available.
     *
     *
     * @return The next available token in the file, including possibly EOF if
     * no more tokens can be produced from the file being analyzed.
     */
    /**
     * Returns the most recently produced Token from the input file. If invoked
     * before the first Token is produced, this method will cause the first
     * Token to be produced from the stream (equivalent to an invocation of
     * {@link next()}).
     *
     *
     * @return The most recently produced Token, or the first Token if no Token
     * has yet been produced
     */
    public Token peek() throws IOException {
        return currentToken != null ? currentToken : next();
    }

    /**
     * Advances the lexer to the next available character in the input file
     * and returns that character.
     *
     * @return The next available character in the input file
     */
    private int scanNextChar() throws IOException {
        currChar = br.read();
        return currChar;
    }

    /**
     * Advances the lexer and returns the next available token from
     * the input file. Will return an instance of Token.EOF if invoked when
     * no more tokens are available.
     *
     *
     * @return The next available token in the file, including possibly EOF if
     * no more tokens can be produced from the file being analyzed.
     */
    public Token next() throws IOException {
        /*
         * Advance the current character to the next possible token, past
         * any whitespace
         */
        while (currChar != -1 && isWhitespace(currChar)) {
            scanNextChar();
        }

        /*
         * Set the next available token depending on the character encountered
         */
        if (currChar == -1) {
            currentToken = makeEOF();
        }
        else if (currChar == '(') {
            currentToken = makeLparen();
        }
        else if (currChar == ')') {
            currentToken = makeRparen();
        }
        else if (currChar == '\'') {
            currentToken = makeQuote();
        }
        else if (currChar == '"') {
            currentToken = makeString();
        }
        else if (currChar == '#') {
            currentToken = makeBoolean();
        }
        else if (isIntegerChar(currChar)) {
            currentToken = makeInteger();
        }
        else if (isIdentifierChar(currChar)) {
            currentToken = makeIdentifier();
        }
        else {
            String message = format(
                    "Syntax error: Unexpected character '%s' in program", currChar);
            throw new RuntimeException(message);
        }

        return currentToken;
    }

    public void discard() {
        currentToken = null;
    }

    /**
     * Generates an IDENTIFIER token. Will advance the current character past
     * all those which comprise the IDENTIFIER.
     *
     * @return An IDENTIFIER Token
     */
    private Token makeIdentifier() throws IOException {
        String value = getCompleteTokenValue(Lexer::isIdentifierChar);
        return Token.makeIdentifier(value);
    }

    /**
     * Generates an INTEGER token. Will advance the current character past
     * all those which comprise the INTEGER.
     *
     * @return An INTEGER Token
     */
    private Token makeInteger() throws IOException {
        String value = getCompleteTokenValue(Lexer::isIntegerChar);
        return Token.makeInteger(value);
    }

    private Token makeBoolean() throws IOException {
        String value = getCompleteTokenValue(Lexer::isIdentifierChar);
        return Token.makeBoolean(value);
    }

    /**
     * Generates an STRING token. Will advance the current character past
     * all those which comprise the STRING, as well as strip away the
     * quotation marks which demarcate the STRING.
     *
     * @return An STRING Token
     */
    private Token makeString() throws IOException {
        String value = getCompleteTokenValue(Lexer::isStringChar);
        value = value.replaceAll("\"", "");

        /*
         * Advance the current character past the quotation mark that ends
         * this token.
         */
        scanNextChar();

        return Token.makeString(value);
    }

    /**
     * Generates a QUOTE token. Will advance the current character past the
     * quote character just scanned
     *
     * @return A QUOTE token
     */
    private Token makeQuote() throws IOException {
        scanNextChar();

        return Token.makeQuote();
    }

    /**
     * Generates a EOF token.
     *
     * @return A QUOTE token
     */
    private Token makeEOF() throws IOException {
        return Token.makeEOF();
    }

    /**
     * Generates a LPAREN token. Will advance the current character past the
     * left parenthesis character just scanned
     *
     * @return A LPAREN token
     */
    private Token makeLparen() throws IOException {
        scanNextChar();
        return Token.makeLparen();
    }

    /**
     * Generates a RPAREN token. Will advance the current character past the
     * right parenthesis character just scanned
     *
     * @return A RPAREN token
     */
    private Token makeRparen() throws IOException {
        scanNextChar();
        return Token.makeRparen();
    }

    /**
     * A convenience method that consumes characters in the stream that comprise
     * a multi-character token and returns a String containing those
     * characters.
     *
     * Using the given Predicate, this method continues consuming characters
     * until the predicate fails or the maximum length for a valid
     * token is reached.
     *
     * @param isValidChar Predicate which determines if a character belongs to
     * a given token type
     *
     * @return A String containing the characters which belong to the Token
     *
     * @throws SchemeInterpreterException If the token exceeds the maximum
     * allowed length
     */
    private String getCompleteTokenValue(Predicate<Integer> isValidChar) throws IOException {
        int length = 0;
        StringBuilder sb = new StringBuilder();

        do {
            sb.append((char) currChar);
            scanNextChar();
            length++;
        }
        while (length <= MAX_TOKEN_LENGTH && isValidChar.test(currChar));

        if (length > MAX_TOKEN_LENGTH) {
            String message = format(
                    "Syntax error: Encountered token longer than %s characters", MAX_TOKEN_LENGTH);
            throw new RuntimeException(message);
        }

        return sb.toString();
    }

    /**
     * A convenience method that tests if a character is EOF or a
     * newline ('\n').
     *
     * @param c The character to test
     *
     * @return True if the character is neither EOF or '\n', false otherwise
     */
    private static boolean isNotNewlineOrEOF(int c) {
        return c != -1 && c != '\n';
    }

    /**
     * A convenience method that tests if a character is one of the allowable
     * "extended characters" which may begin an IDENTIFIER.
     *
     * @param c The character to test
     *
     * @return True if the character is one of the allowed characters, false
     * otherwise.
     */
    private static boolean isExtendedChar(int c) {
        return EXTENDED_CHARS.indexOf(c) != -1;
    }

    /**
     * A convenience method that tests if a character is permitted to be part
     * of a STRING token. A character qualifies if is not a newline (tokens
     * cannot span multiple lines), EOF, or a quotation mark (as this
     * marks the end of the STRING).
     *
     * @param c The character to test
     *
     * @return True if the character may belong to a STRING, false otherwise.
     */
    private static boolean isStringChar(int c) {
        return isNotNewlineOrEOF(c) && c != '"';
    }

    /**
     * A convenience method that tests if a character is permitted to be part
     * of an IDENTIFIER token. A character qualifies if
     * <ul>
     * <li>is neither EOF or '\n' (tokens cannot span multiple lines), and</li>
     * <li>is one of the permitted extended characters, a letter or a digit</li>
     * </ul>
     *
     * @param c The character to test
     *
     * @return True if the character may belong to an IDENTIFIER, false otherwise.
     */
    private static boolean isIdentifierChar(int c) {
        return isNotNewlineOrEOF(c)
                && (isExtendedChar(c)
                || isLetter(c)
                || isDigit(c));
    }

    /**
     * A convenience method that tests if a character is permitted to be part
     * of an INTEGER token. A character qualifies if is a digit.
     *
     * @param c The character to test
     *
     * @return True if the character may belong to an IDENTIFIER, false otherwise.
     */
    private static boolean isIntegerChar(int c) {
        return isDigit(c);
    }

}
