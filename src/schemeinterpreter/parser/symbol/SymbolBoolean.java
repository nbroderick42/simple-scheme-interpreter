/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser.symbol;

import schemeinterpreter.lexer.token.Token;

/**
 *
 * @author nick
 */
public class SymbolBoolean extends Symbol {
    
    private java.lang.Boolean value;

    public SymbolBoolean() {
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
                java.lang.String message = java.lang.String.format("`%s' is not a valid boolean", tokenValue);
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
