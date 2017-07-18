package schemeinterpreter.lexer.token;

/**
 * The definition for the Token and its subtypes. Tokens are produced by the
 * Lexer when analyzing the input source file.
 * 
 * @author nick
 */
public abstract class Token {
    
    /**
     * The internal value for each token. Need only be set for
     */
    private java.lang.String value;

    /** 
     * Static factory method that creates an LPAREN token
     * 
     * @return an LPAREN Token
     */
    public static TokenLparen makeLparen() {
        return new TokenLparen();
    }
    
    /** 
     * Static factory method that creates an RPAREN token
     * 
     * @return an RPAREN Token
     */
    public static Token makeRparen() {
        return new TokenRparen();
    }
    
    /** 
     * Static factory method that creates an QUOTE token
     * 
     * @return a QUOTE Token
     */
    public static TokenQuote makeQuote() {
        return new TokenQuote();
    }
    
    /** 
     * Static factory method that creates an STRING token
     * 
     * @param value the value to set
     * @return an STRING Token
     */
    public static TokenString makeString(java.lang.String value) {
        return new TokenString(value);
    }
    
    /** 
     * Static factory method that creates an IDENTIFIER token
     * 
     * @param value the value to set
     * @return an IDENTIFIER Token
     */
    public static TokenIdentifier makeIdentifier(java.lang.String value) {
        return new TokenIdentifier(value);
    }

    /** 
     * Static factory method that creates an INTEGER token
     * 
     * @param value the value to set
     * @return an INTEGER Token
     */
    public static TokenInteger makeInteger(java.lang.String value) {
        return new TokenInteger(value);
    }
    
    public static TokenBoolean makeBoolean(java.lang.String value) {
        return new TokenBoolean(value);
    }

    /** 
     * Static factory method that creates an EOF token
     * 
     * @return an EOF Token
     */
    public static TokenEOF makeEOF() {
        return new TokenEOF();
    }
    
    /**
     * Returns the underlying value for this token.
     * 
     * @return The underlying value as a String
     */
    public java.lang.String getValue() {
        return value;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.format("( %s, %s )", getClass(), getValue());
    }

    /**
     * @param value the value to set
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }
}
