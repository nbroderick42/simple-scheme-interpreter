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
public class TokenRparen extends Token {
    
    TokenRparen() {
        super.setValue(")");
    }

    public static java.lang.String repr() {
        return ")";
    }
    
}
