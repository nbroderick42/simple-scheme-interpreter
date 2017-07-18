package schemeinterpreter.parser;

import java.util.function.Consumer;
import schemeinterpreter.evaluator.AtomImpl;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public abstract class Symbol {
    
    private Symbol nextSibling, firstChild, lastChild;
        
    private boolean terminal;
    
    private AtomImpl eval;
    
    protected Symbol() {
        this.terminal = false;
    }
    
    public static class S extends Symbol {}
    
    public static class Exprs extends Symbol {}
    
    public static class Expr extends Symbol {}
    
    public static class ListExprs extends Symbol {}
    
    public static class List extends Symbol {}
    
    public static class Lparen extends Symbol {
    
        public Lparen() {
            super.setTerminal(true);
        }
        
        @Override
        public java.lang.String toString() {
            return "(";
        }
        
    }
    
    public static class Rparen extends Symbol {
    
        public Rparen() {
            super.setTerminal(true);
        }
        
        @Override
        public java.lang.String toString() {
            return ")";
        }
        
    }
    
    public static class Quote extends Symbol {
    
        public Quote() {
            super.setTerminal(true);
        }
        
        @Override
        public java.lang.String toString() {
            return "'";
        }
        
    }
    
    public static class Identifier extends Symbol {
    
        private java.lang.String value;
        
        public Identifier() {
            super.setTerminal(true);
        }
        
        @Override
        public void acceptToken(Token token) {
            this.value = token.getValue();
        }
        
        @Override
        public java.lang.String toString() {
            return value;
        }
        
        public java.lang.String getValue() {
            return value;
        }
        
        @Override
        public java.lang.String toFormattedString() {
            return java.lang.String.format("Identifier: [%s]", value);
        }
        
    }
    
    public static class Integer extends Symbol {
    
        private java.lang.Integer value;
        
        public Integer() {
            super.setTerminal(true);
        }
        
        @Override
        public void acceptToken(Token token) {
            this.value = java.lang.Integer.valueOf(token.getValue());
        }
        
        @Override
        public java.lang.String toString() {
            return value.toString();
        }
        
        @Override
        public java.lang.String toFormattedString() {
            return java.lang.String.format("Integer: [%d]", value);
        }
        
        public java.lang.Integer getValue() {
            return value;
        }
    }
    
    public static class String extends Symbol {
    
        private java.lang.String value;
        
        public String() {
            super.setTerminal(true);
        }
        
        @Override
        public void acceptToken(Token token) {
            this.value = token.getValue();
        }
        
        @Override
        public java.lang.String toString() {
            return value;
        }
        
        @Override
        public java.lang.String toFormattedString() {
            return java.lang.String.format("String: [%s]", value);
        }
        
        public java.lang.String getValue() {
            return value;
        }
        
    }
    
    public static class Boolean extends Symbol {
    
        private java.lang.Boolean value;
        
        public Boolean() {
            super.setTerminal(true);
        }
        
        @Override
        public void acceptToken(Token token) {
            java.lang.String tokenValue = token.getValue();
            
            switch (tokenValue) {
                case "#t":
                    this.value = true;
                    break;
                case "#f":
                    this.value = false;
                    break;
                default:
                    java.lang.String message = 
                            java.lang.String.format("`%s' is not a valid boolean", tokenValue);
                    throw new RuntimeException(message);
            }
        }
        
        @Override
        public java.lang.String toString() {
            return value.toString();
        }
        
        @Override
        public java.lang.String toFormattedString() {
            return java.lang.String.format("String: [%s]", value);
        }
        
        public java.lang.Boolean getValue() {
            return value;
        }
        
    }
    
    public static class EOF extends Symbol {
        
        public EOF() {
            super.setTerminal(true);
        }
        
        @Override
        public java.lang.String toString() {
            return "";
        }       
        
    }
    
    public void addChild(Symbol s) {
        if (firstChild == null) {
            firstChild = s;
        }
        else {
            lastChild.setNextSibling(s);
        }
        
        lastChild = s;
    }
    
    public void setNextSibling(Symbol nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public Symbol getFirstChild() {
        return firstChild;
    }
    
    public Symbol getNextSibling() {
        return nextSibling;
    }
    
    public void forEachChild(Consumer<Symbol> consumer) {
        for (Symbol curr = getFirstChild(); curr != null; curr = curr.getNextSibling()) {
            consumer.accept(curr);
        }
    }
    
    public static Symbol makeStartSymbol() {
        return new Symbol.S();
    }

    public void acceptToken(Token token) {}
    
    public boolean isTerminal() {
        return terminal;
    }
    
    public java.lang.String toFormattedString() {
        return toString();
    }

    /**
     * @param terminal the terminal to set
     */
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public AtomImpl getEval() {
        return eval;
    }

    public void setEval(AtomImpl eval) {
        this.eval = eval;
    }

    public boolean isEOF() {
        return this instanceof EOF;
    }

}
