/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter;

/**
 *
 * @author nick
 */
@SuppressWarnings("serial")
public class SchemeInterpreterException extends RuntimeException {

    /**
     * Creates a new instance of <code>SchemeInterpreterException</code> without detail message.
     */
    public SchemeInterpreterException() {
    }

    /**
     * Constructs an instance of <code>SchemeInterpreterException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SchemeInterpreterException(String msg) {
        super(msg);
    }
}
