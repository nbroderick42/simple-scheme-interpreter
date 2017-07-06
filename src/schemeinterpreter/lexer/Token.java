/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.lexer;

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
    
    public static class Lparen extends Token {
        
        private Lparen() {
            super.setValue("(");
        }
        
        public static java.lang.String repr() {
            return "(";
        }
        
    }
    
    public static class Rparen extends Token {
    
        private Rparen() {
            super.setValue(")");
        }
        
        public static java.lang.String repr() {
            return ")";
        }
        
    }
    
    public static class Quote extends Token {
    
        private Quote() {
            super.setValue("'");
        }
        
        public static java.lang.String repr() {
            return "'";
        }
        
    }
    
    public static class Identifier extends Token {
    
        private Identifier(java.lang.String value) {
            super.setValue(value);
        }
        
        public static java.lang.String repr() {
            return "[identifier]";
        }
        
    }
    
    public static class Integer extends Token {
    
        private Integer(java.lang.String value) {
            super.setValue(value);
        }
        
        public static java.lang.String repr() {
            return "[integer]";
        }
        
    }
    
    public static class String extends Token {
    
        private String(java.lang.String value) {
            super.setValue(value);
        }
        
        public static java.lang.String repr() {
            return "[string]";
        }
        
    }
    
    public static class EOF extends Token {
    
        private EOF() {
            super.setValue("EOF");
        }
        
        public static java.lang.String repr() {
            return "[eof]";
        }
        
    }
    
    /** 
     * Static factory method that creates an LPAREN token
     * 
     * @return an LPAREN Token
     */
    public static Lparen makeLparen() {
        return new Lparen();
    }
    
    /** 
     * Static factory method that creates an RPAREN token
     * 
     * @return an RPAREN Token
     */
    public static Token makeRparen() {
        return new Rparen();
    }
    
    /** 
     * Static factory method that creates an QUOTE token
     * 
     * @return a QUOTE Token
     */
    public static Quote makeQuote() {
        return new Quote();
    }
    
    /** 
     * Static factory method that creates an STRING token
     * 
     * @param value the value to set
     * @return an STRING Token
     */
    public static String makeString(java.lang.String value) {
        return new String(value);
    }
    
    /** 
     * Static factory method that creates an IDENTIFIER token
     * 
     * @param value the value to set
     * @return an IDENTIFIER Token
     */
    public static Identifier makeIdentifier(java.lang.String value) {
        return new Identifier(value);
    }

    /** 
     * Static factory method that creates an INTEGER token
     * 
     * @param value the value to set
     * @return an INTEGER Token
     */
    public static Integer makeInteger(java.lang.String value) {
        return new Integer(value);
    }

    /** 
     * Static factory method that creates an EOF token
     * 
     * @return an EOF Token
     */
    public static EOF makeEOF() {
        return new EOF();
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
