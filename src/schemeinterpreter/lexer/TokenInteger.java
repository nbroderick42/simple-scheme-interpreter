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
public class TokenInteger extends Token {

    public static String repr() {
        return "[integer]";
    }
    
    TokenInteger(String value) {
        super.setValue(value);
    }

}
