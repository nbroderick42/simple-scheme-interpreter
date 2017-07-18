/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

/**
 *
 * @author nick
 */
public class SchemeException extends RuntimeException {

    private final Atom value;

    /**
     * Creates a new instance of <code>SchemeException</code> without detail message.
     */
    public SchemeException(Atom value) {
        super("Uncaught exception raised with value " + value);
        this.value = value;
    }

    public Atom getValue() {
        return value;
    }
}
