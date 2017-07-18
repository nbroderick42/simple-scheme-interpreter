/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator.atom;

import java.util.function.Function;
import schemeinterpreter.evaluator.Evaluator;
import schemeinterpreter.evaluator.Frame;

/**
 *
 * @author nick
 */
public class AtomLambda extends AtomImpl implements AtomProcedure {
    
    Frame closingFrame;
    AtomList params;
    AtomList exprs;

    private AtomLambda() {
    }

    private AtomLambda(Frame closingFrame, AtomList params, AtomList exprs) {
        this.closingFrame = closingFrame;
        this.params = params;
        this.exprs = exprs;
    }

    public static AtomLambda make(Evaluator evaluator, AtomList params, AtomList exprs) {
        return new AtomLambda(evaluator.getCurrentFrame(), params, exprs);
    }

    @Override
    public Atom evaluate(Evaluator evaluator) {
        return evaluator.evaluate(this);
    }

    @Override
    public Atom apply(Evaluator evaluator, AtomList args) {
        Function<Frame, Atom> impl = (schemeinterpreter.evaluator.Frame scope) -> {
            AtomList currParams = params;
            AtomList currArgs = args;
            while (!currParams.isEmpty() && !currArgs.isEmpty()) {
                AtomIdentifier id = (AtomIdentifier) currParams.getHead();
                Atom expr = args.getHead().evaluate(evaluator);
                scope.bind(id, expr);
                currParams = currParams.getTail();
                currArgs = currArgs.getTail();
            }
            return evaluateListAndTakeLast(evaluator, exprs);
        };
        return evaluator.evaluateInScope(closingFrame, impl);
    }

    public static Atom evaluateListAndTakeLast(Evaluator evaluator, AtomList exprs) {
        return exprs.stream().map((atom) -> atom.evaluate(evaluator)).reduce(AtomVoid.getInstance(), AtomLambda::takeLast);
    }

    @Override
    public java.lang.String toString() {
        return "<lambda>";
    }

    private static Atom takeLast(Atom first, Atom second) {
        return second;
    }
    
}
