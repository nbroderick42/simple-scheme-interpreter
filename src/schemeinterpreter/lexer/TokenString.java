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
public class TokenString extends Token {

    public static String repr() {
        return "[string]";
    }
    
    TokenString(String value) {
        super.setValue(value);
    }

}
