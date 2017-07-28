/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Map;
import java.util.function.Function;
import schemeinterpreter.parser.Symbol;

/**
 *
 * @author nick
 */
public class Evaluator {

    private static Evaluator INSTANCE;
    
    public static Evaluator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Evaluator();
        }
        return INSTANCE;
    }

    public static void evaluate(Symbol ast) {        
        AtomList atoms = (AtomList) ast.getEval();
        atoms.forEach(atom -> {
            Atom eval = atom.evaluate();
            System.out.println("=> " + eval);
        });
    }

    static Atom evaluate(Atom atom) {
        return atom.evaluate();
    }

    static Atom evaluate(AtomIdentifier id) {
        return getInstance().evaluateImpl(id);
    }
    
    static Atom evaluate(AtomInteger integer) {
        return integer;
    }

    static Atom evaluate(AtomString string) {
        return string;
    }

    static Atom evaluate(AtomList list) {
        if (list.isLazy() || list.isEmpty()) {
            return list;
        }
        else {
            Atom proc = list.getHead().evaluate();
            assertTrue(proc.isProcedure(), "first argument is not a procedure");

            AtomProcedure op = (AtomProcedure) proc;

            return op.apply(list.getTail());
        }
    }

    static Atom evaluate(AtomLambda op) {
        return op;
    }

    static Atom evaluate(BuiltinProcedure op) {
        return op;
    }

    static Atom evaluate(AtomBoolean bool) {
        return bool;
    }

    static Atom evaluate(AtomVoid v) {
        return v;
    }

    static Atom evaluateInLocalScope(Function<Frame, Atom> callback) {
        return getInstance().evaluateInLocalScopeImpl(callback);
    }
    

    static Atom evaluateInScope(Frame scope, Function<Frame, Atom> callback) {
        return getInstance().evaluateInScopeImpl(scope, callback);
    }
    

    static void bindToCurrentFrame(AtomIdentifier id, Atom value) {
        getInstance().bindToCurrentFrameImpl(id, value);
    }

    static void bindToCurrentFrame(Map.Entry<AtomIdentifier, Atom> entry) {
        getInstance().bindToCurrentFrameImpl(entry);
    }
    
    static Frame getCurrentFrame() {
        return getInstance().getCurrentFrameImpl();
    }
    
    static boolean isBound(AtomIdentifier id) {
        return getInstance().isBoundImpl(id);
    }
    
    static Atom resolve(AtomIdentifier id) {
        return getInstance().resolveImpl(id);
    }
      
    static boolean isConditionTrue(Atom pair) {
            assertTrue(pair.isListPair(), "expected pair of atoms");
            AtomList arg = (AtomList) pair;

            Atom expr = arg.getHead().evaluate();
            if (!expr.isBoolean()) {
                return true;
            }

            AtomBoolean test = (AtomBoolean) expr;
            return test.isTrue();
    }

    static Atom evaluateAndTakeSecond(Atom atom) {
        assertTrue(atom.isListPair(), "expected pair of atoms");
        AtomList pair = (AtomList) atom;
        
        return pair.getTail().getHead().evaluate();
    }
    
    static Atom evaluateListAndTakeLast(AtomList exprs) {
        return exprs.stream()
                .map(Atom::evaluate)
                .reduce(AtomVoid.getInstance(), Evaluator::takeLast);
    }

    private static Atom takeLast(Atom first, Atom second) {
        return second;
    }

    public static void assertTrue(boolean test, String message) {
        if (!test) {
            throw new SchemeEvaluationError(message);
        }
    }

    private Frame currentFrame;
    
    private Evaluator() {
        this.currentFrame = makeGlobalFrame();
    }
    
    private Atom evaluateImpl(AtomIdentifier id) {
        if (id.isLazy()) {
            return id;
        }
        else {
            return currentFrame.resolve(id);
        }
    }
    
    private Atom evaluateInLocalScopeImpl(Function<Frame, Atom> callback) {
        currentFrame = currentFrame.attachNewFrame();
        Atom result = callback.apply(currentFrame);
        currentFrame = currentFrame.getParent();
        
        return result;
    }
    
    private Atom evaluateInScopeImpl(Frame scope, Function<Frame, Atom> callback) {
        Frame prevFrame = currentFrame;
        currentFrame = scope;
        
        Atom result = callback.apply(scope);
        
        currentFrame = prevFrame;
        
        return result;
    }
    
    private void bindToCurrentFrameImpl(AtomIdentifier id, Atom value) {
        currentFrame.bind(id, value);
    }
    
    private void bindToCurrentFrameImpl(Map.Entry<AtomIdentifier, Atom> entry) {
        currentFrame.bind(entry.getKey(), entry.getValue());
    }
    
    private Frame getCurrentFrameImpl() {
        return currentFrame;
    }
    
    private Frame makeGlobalFrame() {
        Frame globalFrame = Frame.makeGlobalFrame();
        
        for (BuiltinProcedure op : BuiltinProcedure.values()) {
            globalFrame.bind(op.getToken(), op);
        }

        return globalFrame;
    }
    
    private boolean isBoundImpl(AtomIdentifier id) {
        return currentFrame.isBound(id);
    }
    
    private Atom resolveImpl(AtomIdentifier id) {
        return currentFrame.resolve(id);
    }

}
