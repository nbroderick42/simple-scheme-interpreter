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

    private final Atom raisedValue;
    
    /**
     * Creates a new instance of <code>SchemeException</code> without detail message.
     */
    public SchemeException(Atom raisedValue) {
        super("Uncaught exception raised with value " + raisedValue);
        this.raisedValue = raisedValue;
    }
    
    public Atom getRaisedValue() {
        return raisedValue;
    }
}
