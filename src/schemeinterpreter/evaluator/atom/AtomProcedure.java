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
public interface AtomProcedure extends Atom {

    public Atom apply(Evaluator evaluator, AtomList args);
    
}
