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
public abstract class Token {

    protected java.lang.String value;
    
    public static class Lparen extends Token {
        
        private Lparen() {
            this.value = "(";
        }
        
        public static java.lang.String repr() {
            return "(";
        }
        
    }
    
    public static class Rparen extends Token {
    
        private Rparen() {
            this.value = ")";
        }
        
        public static java.lang.String repr() {
            return ")";
        }
        
    }
    
    public static class Quote extends Token {
    
        private Quote() {
            this.value = "'";
        }
        
        public static java.lang.String repr() {
            return "'";
        }
        
    }
    
    public static class Identifier extends Token {
    
        private Identifier(java.lang.String value) {
            this.value = value;
        }
        
        public static java.lang.String repr() {
            return "[identifier]";
        }
        
    }
    
    public static class Integer extends Token {
    
        private Integer(java.lang.String value) {
            this.value = value;
        }
        
        public static java.lang.String repr() {
            return "[integer]";
        }
        
    }
    
    public static class String extends Token {
    
        private String(java.lang.String value) {
            this.value = value;
        }
        
        public static java.lang.String repr() {
            return "[string]";
        }
        
    }
    
    public static class EOF extends Token {
    
        private EOF() {
            this.value = "EOF";
        }
        
        public static java.lang.String repr() {
            return "[eof]";
        }
        
    }
    
    public static Lparen makeLparen() {
        return new Lparen();
    }

    public static Token makeRparen() {
        return new Rparen();
    }
    
    public static Quote makeQuote() {
        return new Quote();
    }

    public static String makeString(java.lang.String value) {
        return new String(value);
    }

    public static Identifier makeIdentifier(java.lang.String value) {
        return new Identifier(value);
    }

    public static Integer makeInteger(java.lang.String value) {
        return new Integer(value);
    }

    public static EOF makeEOF() {
        return new EOF();
    }

    public java.lang.String getValue() {
        return value;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.format("( %s, %s )", getClass(), value);
    }
}
