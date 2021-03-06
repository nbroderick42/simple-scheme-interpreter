/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.function.Function;
import static schemeinterpreter.evaluator.Evaluator.evaluateInScope;
import static schemeinterpreter.evaluator.Evaluator.evaluateListAndTakeLast;

/**
 *
 * @author nick
 */
public class AtomLambda extends AtomImpl implements AtomProcedure {

    public static AtomLambda make(Frame closingFrame, AtomList params, AtomList exprs) {
        return new AtomLambda(closingFrame, params, exprs);
    }

    private Frame closingFrame;
    private AtomList params;
    private AtomList exprs;

    private AtomLambda() {
    }

    private AtomLambda(Frame closingFrame, AtomList params, AtomList exprs) {
        this.closingFrame = closingFrame;
        this.params = params;
        this.exprs = exprs;
    }


    @Override
    public Atom evaluate() {
        return Evaluator.evaluate(this);
    }

    @Override
    public Atom apply(AtomList args) {
        Function<Frame, Atom> impl = (schemeinterpreter.evaluator.Frame scope) -> {
            AtomList currParams = params;
            AtomList currArgs = args;
            while (!currParams.isEmpty() && !currArgs.isEmpty()) {
                AtomIdentifier id = (AtomIdentifier) currParams.getHead();
                Atom expr = args.getHead().evaluate();
                scope.bind(id, expr);
                currParams = currParams.getTail();
                currArgs = currArgs.getTail();
            }
            return evaluateListAndTakeLast(exprs);
        };
        return evaluateInScope(closingFrame, impl);
    }


    @Override
    public String toString() {
        return "<lambda>";
    }


}
