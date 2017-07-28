/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

/**
 *
 * @author nick
 */
public class SchemeSyntaxError extends RuntimeException {

    /**
     * Creates a new instance of <code>SchemeSyntaxError</code> without detail message.
     */
    public SchemeSyntaxError() {
    }

    /**
     * Constructs an instance of <code>SchemeSyntaxError</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SchemeSyntaxError(String msg) {
        super("Syntax error: " + msg);
    }
}
