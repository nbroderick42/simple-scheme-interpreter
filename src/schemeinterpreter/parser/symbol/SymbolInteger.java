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
public class SymbolInteger extends Symbol {

    private Integer value;

    public SymbolInteger() {
        super.setTerminal(true);
    }

    @Override
    public void acceptToken(Token token) {
        this.value = Integer.valueOf(token.getValue());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toFormattedString() {
        return String.format("Integer: [%d]", value);
    }

    public Integer getValue() {
        return value;
    }

}
