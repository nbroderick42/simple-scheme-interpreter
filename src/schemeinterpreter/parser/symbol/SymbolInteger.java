/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser.symbol;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import schemeinterpreter.lexer.token.Token;

/**
 *
 * @author nick
 */
public class SymbolInteger extends Symbol {

    private java.lang.Integer value;

    public SymbolInteger() {
        super.setTerminal(true);
    }

    @Override
    public void acceptToken(Token token) {
        this.value = valueOf(token.getValue());
    }

    @Override
    public java.lang.String toString() {
        return value.toString();
    }

    @Override
    public java.lang.String toFormattedString() {
        return format("Integer: [%d]", value);
    }

    public java.lang.Integer getValue() {
        return value;
    }

}
