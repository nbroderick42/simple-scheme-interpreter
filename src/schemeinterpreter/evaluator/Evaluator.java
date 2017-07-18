/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import schemeinterpreter.evaluator.atom.AtomVoid;
import schemeinterpreter.evaluator.atom.AtomBoolean;
import schemeinterpreter.evaluator.atom.AtomImpl;
import schemeinterpreter.evaluator.atom.AtomString;
import schemeinterpreter.evaluator.atom.Atom;
import schemeinterpreter.evaluator.atom.AtomProcedure;
import schemeinterpreter.evaluator.atom.AtomInteger;
import schemeinterpreter.evaluator.atom.AtomLambda;
import schemeinterpreter.evaluator.atom.AtomList;
import schemeinterpreter.evaluator.atom.AtomIdentifier;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import schemeinterpreter.parser.AbstractSyntaxTree;
import schemeinterpreter.parser.symbol.Symbol;

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
        AtomList atoms = (AtomList) startSymbol.getEval();
        atoms.forEach(atom -> {
            Atom eval = atom.evaluate(this);
            System.out.println("=> " + eval);
        });
    }
    
    public Atom evaluate(Atom atom) {
        return atom.evaluate(this);
    }
    
    public Atom evaluate(AtomIdentifier id) {
        if (id.isLazy()) {
            return id;
        }
        else {
            return currentFrame.resolve(id);
        }
    }
    
    public Atom evaluate(AtomInteger integer) {
        return integer;
    }
    
    public Atom evaluate(AtomString string) {
        return string;
    }
    
    public Atom evaluate(AtomList list) {
        if (list.isLazy() || list.isEmpty()) {
            return list;
        }
        else {
            Atom proc = list.getHead().evaluate(this);
            assertTrue(proc.isOperation(), "first argument is not a procedure");
            
            AtomProcedure op = (AtomProcedure) proc;
            
            return op.apply(this, list.getTail());
        }
    }
    
    public Atom evaluate(AtomLambda op) {
        return op;
    }
    
    
    public Atom evaluate(BuiltinProcedure op) {
        return op;
    }
    
    public AtomImpl evaluate(AtomBoolean bool) {
        return bool;
    }
    
    public AtomImpl evaluate(AtomVoid v) {
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

    public void bindToCurrentFrame(AtomIdentifier id, Atom value) {
        currentFrame.bind(id, value);
    }
    
    public void bindToCurrentFrame(Map.Entry<AtomIdentifier, Atom> entry) {
        currentFrame.bind(entry.getKey(), entry.getValue());
    }

    private Frame makeGlobalFrame() {
        Frame globalFrame = Frame.makeGlobalFrame();

        for (BuiltinProcedure op : BuiltinProcedure.values()) {
            globalFrame.bind(op.getToken(), op);
        }
      
        return globalFrame;
    }
    
    public Frame getCurrentFrame() {
        return currentFrame;
    }
    
    public boolean isBound(AtomIdentifier id) {
        return currentFrame.isBound(id);
    }
    
    public Atom getValue(AtomIdentifier id) {
        return currentFrame.resolve(id);
    }
    
    public static void assertTrue(boolean test, String message) {
        if (!test) {
            throw new EvaluationException(message);
        }
    }
    
}
