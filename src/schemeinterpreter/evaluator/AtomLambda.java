/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.function.Function;
import static schemeinterpreter.evaluator.Evaluator.evaluateInScope;
import static schemeinterpreter.evaluator.Evaluator.evaluate;

/**
 *
 * @author nick
 */
public class AtomLambda extends AtomImpl implements AtomProcedure {

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

    public static AtomLambda make(Frame closingFrame, AtomList params, AtomList exprs) {
        return new AtomLambda(closingFrame, params, exprs);
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

    public static Atom evaluateListAndTakeLast(AtomList exprs) {
        return exprs.stream()
                .map((atom) -> atom.evaluate())
                .reduce(AtomVoid.getInstance(), AtomLambda::takeLast);
    }

    @Override
    public java.lang.String toString() {
        return "<lambda>";
    }

    private static Atom takeLast(Atom first, Atom second) {
        return second;
    }

}
