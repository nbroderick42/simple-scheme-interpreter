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
public class SchemeFatalError extends RuntimeException {

    /**
     * Creates a new instance of <code>SchemeFatalError</code> without detail message.
     */
    public SchemeFatalError() {
    }

    /**
     * Constructs an instance of <code>SchemeFatalError</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SchemeFatalError(String msg) {
        super("Fatal error: " + msg);
    }
}
