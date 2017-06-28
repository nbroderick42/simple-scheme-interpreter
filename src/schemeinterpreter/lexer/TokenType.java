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
public enum TokenType {
    
    LPAREN("("),
    RPAREN(")"),
    EOF("EOF"),
    INTEGER("[int]"),
    STRING("[string]"),
    IDENTIFIER("[identifier]"),
    QUOTE("'");
    
    private final String repr;
    
    private TokenType(String repr) {
        this.repr = repr;
    }
    
    @Override
    public String toString() {
        return repr;
    }
}
