package schemeinterpreter.lexer;

/**
 * The definition for the Token and its subtypes. Tokens are produced by the
 * Lexer when analyzing the input source file.
 *
 * @author nick
 */
public abstract class Token {

    /**
     * Static factory method that creates an LPAREN token
     *
     * @return an LPAREN Token
     */
    static TokenLparen makeLparen() {
        return new TokenLparen();
    }
    
    static TokenNewline makeNewline() {
        return new TokenNewline();
    }

    /**
     * Static factory method that creates an RPAREN token
     *
     * @return an RPAREN Token
     */
    static Token makeRparen() {
        return new TokenRparen();
    }

    /**
     * Static factory method that creates an QUOTE token
     *
     * @return a QUOTE Token
     */
    static TokenQuote makeQuote() {
        return new TokenQuote();
    }

    /**
     * Static factory method that creates an STRING token
     *
     * @param value the value to set
     * @return an STRING Token
     */
    static TokenString makeString(String value) {
        return new TokenString(value);
    }

    /**
     * Static factory method that creates an IDENTIFIER token
     *
     * @param value the value to set
     * @return an IDENTIFIER Token
     */
    static TokenIdentifier makeIdentifier(String value) {
        return new TokenIdentifier(value);
    }

    /**
     * Static factory method that creates an INTEGER token
     *
     * @param value the value to set
     * @return an INTEGER Token
     */
    static TokenInteger makeInteger(String value) {
        return new TokenInteger(value);
    }

    static TokenBoolean makeBoolean(String value) {
        return new TokenBoolean(value);
    }

    /**
     * Static factory method that creates an EOF token
     *
     * @return an EOF Token
     */
    static TokenEOF makeEOF() {
        return new TokenEOF();
    }
    
    /**
     * The internal value for each token. Need only be set for
     */
    private String value;

    /**
     * Returns the underlying value for this token.
     *
     * @return The underlying value as a String
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("( %s, %s )", getClass(), getValue());
    }

    /**
     * @param value the value to set
     */
    void setValue(String value) {
        this.value = value;
    }
    
    boolean isNewline() {
        return this instanceof TokenNewline;
    }
}
