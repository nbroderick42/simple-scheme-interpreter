/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser.symbol;

import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public class SymbolIdentifier extends Symbol {

    private String value;

    public SymbolIdentifier() {
        super.setTerminal(true);
    }

    @Override
    public void acceptToken(Token token) {
        this.value = token.getValue();
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toFormattedString() {
        return String.format("Identifier: [%s]", value);
    }

}
