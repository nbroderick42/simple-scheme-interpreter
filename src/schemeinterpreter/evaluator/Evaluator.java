/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import schemeinterpreter.parser.AbstractSyntaxTree;
import schemeinterpreter.parser.Symbol;

/**
 *
 * @author nick
 */
public class Evaluator {

    private final Symbol startSymbol;
    
    private Frame currentFrame;

    private Evaluator(AbstractSyntaxTree ast) {
        this.startSymbol = ast.getRoot();
        this.currentFrame = makeGlobalFrame();
    }
    
    public static void evaluate(AbstractSyntaxTree ast) {
        new Evaluator(ast).evaluate();
    }
    
    
    private void evaluate() {
        AtomImpl.List atoms = (AtomImpl.List) startSymbol.getEval();
        atoms.forEach(atom -> {
            Atom eval = atom.evaluate(this);
            System.out.println("=> " + eval);
        });
    }
    
    public Atom evaluate(Atom atom) {
        return atom.evaluate(this);
    }
    
    public Atom evaluate(AtomImpl.Identifier id) {
        if (id.isLazy()) {
            return id;
        }
        else {
            return currentFrame.resolve(id);
        }
    }
    
    public Atom evaluate(AtomImpl.Integer integer) {
        return integer;
    }
    
    public Atom evaluate(AtomImpl.String string) {
        return string;
    }
    
    public Atom evaluate(AtomImpl.List list) {
        if (list.isLazy() || list.isEmpty()) {
            return list;
        }
        else {
            Atom proc = list.getHead().evaluate(this);
            assertTrue(proc.isOperation(), "first part of procedure call must be an operation");
            
            AtomImpl.Operation op = (AtomImpl.Operation) proc;
            
            return op.apply(this, list.getTail());
        }
    }
    
    public Atom evaluate(AtomImpl.Lambda op) {
        return op;
    }
    
    
    public Atom evaluate(BuiltinOperation op) {
        return op;
    }
    
    public AtomImpl evaluate(AtomImpl.Boolean bool) {
        return bool;
    }
    
    public AtomImpl evaluate(AtomImpl.Void v) {
        return null;
    }
    
    public Atom evaluateInLocalScope(Function<Frame, Atom> callback) {
        currentFrame = currentFrame.attachNewFrame();
        Atom result = callback.apply(currentFrame);
        currentFrame = currentFrame.getParent();

        return result;
    }
    
    public Atom evaluateInScope(Frame scope, Function<Frame, Atom> callback) {
        Frame prevFrame = currentFrame;
        currentFrame = scope;
        
        Atom result = callback.apply(scope);
        
        currentFrame = prevFrame;
        
        return result;
    }

    public void bindToCurrentFrame(AtomImpl.Identifier id, Atom value) {
        currentFrame.bind(id, value);
    }
    
    public void bindToCurrentFrame(Map.Entry<AtomImpl.Identifier, Atom> entry) {
        currentFrame.bind(entry.getKey(), entry.getValue());
    }

    private Frame makeGlobalFrame() {
        Frame globalFrame = Frame.makeGlobalFrame();

        for (BuiltinOperation op : BuiltinOperation.values()) {
            globalFrame.bind(op.getToken(), op);
        }
      
        return globalFrame;
    }
    
    public Frame getCurrentFrame() {
        return currentFrame;
    }
    
    public boolean isBound(AtomImpl.Identifier id) {
        return currentFrame.isBound(id);
    }
    
    public Atom getValue(AtomImpl.Identifier id) {
        return currentFrame.resolve(id);
    }
    
    public static void assertTrue(boolean test, String message) {
        if (!test) {
            throw new EvaluationException(message);
        }
    }
    
}
