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
public class TokenEOF extends Token {

    TokenEOF() {
        super.setValue("EOF");
    }

    public static java.lang.String repr() {
        return "[eof]";
    }

}
