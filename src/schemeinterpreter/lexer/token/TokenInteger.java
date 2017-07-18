/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.lexer.token;

/**
 *
 * @author nick
 */
public class TokenInteger extends Token {
    
    TokenInteger(java.lang.String value) {
        super.setValue(value);
    }

    public static java.lang.String repr() {
        return "[integer]";
    }
    
}
