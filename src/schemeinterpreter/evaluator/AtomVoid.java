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
public class AtomVoid extends AtomImpl {

    private static final AtomVoid INSTANCE = new AtomVoid();

    @Override
    public Atom evaluate() {
        return Evaluator.getInstance().evaluate(this);
    }

    public static AtomVoid getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "<void>";
    }

}
