/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.util.function.Consumer;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public abstract class Symbol {
    
    private Symbol nextSibling, firstChild, lastChild;
    
    public static class S extends Symbol {

        @Override
        public boolean isTerminal() {
            return false;
        }
    
    }
    
    public static class Exprs extends Symbol{
    
        @Override
        public boolean isTerminal() {
            return false;
        }
        
    }
    
    public static class Expr extends Symbol {
    
        @Override
        public boolean isTerminal() {
            return false;
        }
        
    }
    
    public static class ListExprs extends Symbol {
    
        @Override
        public boolean isTerminal() {
            return false;
        }
        
    }
    
    public static class List extends Symbol {
    
        @Override
        public boolean isTerminal() {
            return false;
        }
        
    }
    
    public static class Lparen extends Symbol {
    
        @Override
        public boolean isTerminal() {
            return true;
        }
        
        @Override
        public java.lang.String toString() {
            return "(";
        }
        
    }
    
    public static class Rparen extends Symbol {
    
        @Override
        public boolean isTerminal() {
            return true;
        }
        
        @Override
        public java.lang.String toString() {
            return ")";
        }
        
    }
    
    public static class Quote extends Symbol {
    
        @Override
        public boolean isTerminal() {
            return true;
        }
        
        @Override
        public java.lang.String toString() {
            return "'";
        }
        
    }
    
    public static class Identifier extends Symbol {
    
        private java.lang.String value;
        
        @Override
        public boolean isTerminal() {
            return true;
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
            return java.lang.String.format("Identifier: [%s]", value);
        }
        
    }
    
    public static class Integer extends Symbol {
    
        private java.lang.Integer value;
        
        @Override
        public boolean isTerminal() {
            return true;
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
        
    }
    
    public static class String extends Symbol {
    
        private java.lang.String value;
        
        @Override
        public boolean isTerminal() {
            return true;
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
        
    }
    
    public static class EOF extends Symbol {
        
        @Override
        public boolean isTerminal() {
            return true;
        }
        
        @Override
        public java.lang.String toString() {
            return "";
        }
        
    }
    
    public void addChild(Symbol s) {
        if (firstChild == null) {
            firstChild = s;
            lastChild = s;
        }
        else {
            lastChild.setNextSibling(s);
            lastChild = s;
        }
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
    
    public static Symbol getStartSymbol() {
        return new Symbol.S();
    }

    public void acceptToken(Token token) {}
    
    public abstract boolean isTerminal();
    
    public java.lang.String toFormattedString() {
        return toString();
    }
    
}
