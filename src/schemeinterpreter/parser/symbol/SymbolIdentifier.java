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
public class SymbolIdentifier extends Symbol {
    
    private java.lang.String value;

    public SymbolIdentifier() {
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