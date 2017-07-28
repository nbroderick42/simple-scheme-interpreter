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
public class SchemeEvaluationError extends RuntimeException {

    /**
     * Creates a new instance of <code>SchemeEvaluationError</code> without detail message.
     */
    public SchemeEvaluationError() {
    }

    /**
     * Constructs an instance of <code>SchemeEvaluationError</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SchemeEvaluationError(String msg) {
        super("Evaluation error: " + msg);
    }
}
