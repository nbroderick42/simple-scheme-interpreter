/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator.atom;

import schemeinterpreter.evaluator.Evaluator;

/**
 *
 * @author nick
 */
public class AtomVoid extends AtomImpl {
    
    private static final AtomVoid INSTANCE = new AtomVoid();

    @Override
    public AtomImpl evaluate(Evaluator evaluator) {
        return evaluator.evaluate(this);
    }

    public static AtomVoid getInstance() {
        return INSTANCE;
    }

    @Override
    public java.lang.String toString() {
        return "<void>";
    }
    
}
