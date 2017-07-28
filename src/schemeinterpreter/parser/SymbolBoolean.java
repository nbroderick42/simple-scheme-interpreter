/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public class SymbolBoolean extends Symbol {

    private Boolean value;

    public SymbolBoolean() {
        super.setTerminal(true);
    }

    @Override
    public void acceptToken(Token token) {
        String tokenValue = token.getValue();
        switch (tokenValue) {
            case "#t":
                this.value = true;
                break;
            case "#f":
                this.value = false;
                break;
            default:
                String message = String.format("`%s' is not a valid boolean", tokenValue);
                throw new RuntimeException(message);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toFormattedString() {
        return String.format("String: [%s]", value);
    }

    public Boolean getValue() {
        return value;
    }

}
