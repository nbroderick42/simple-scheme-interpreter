/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import static schemeinterpreter.evaluator.Evaluator.assertTrue;

/**
 *
 * @author nick
 */
public interface Atom {
    
    public boolean isLazy();
    
    public Atom evaluate(Evaluator evaluator);

    public default boolean isIdentifier() {
        return this instanceof AtomImpl.Identifier;
    }

    public default boolean isString() {
        return this instanceof AtomImpl.String;
    }

    public default boolean isInteger() {
        return this instanceof AtomImpl.Integer;
    }

    public default boolean isVoid() {
        return this instanceof AtomImpl.Void;
    }

    public default boolean isBoolean() {
        return this instanceof AtomImpl.Boolean;
    }

    public default boolean isList() {
        return this instanceof AtomImpl.List;
    }

    public default boolean isOperation() {
        return this instanceof AtomImpl.Operation;
    }
    
    public default boolean isListPair() {
        if (this instanceof AtomImpl.List) {
            AtomImpl.List list = (AtomImpl.List) this;
            return list.isPair();
        }
        
        return false;
    }
    
    public static void assertIsBoolean(Atom atom) {
        assertTrue(atom.isBoolean(), "non-boolean result in expression expecting boolean");
    }
    
    public static void assertIsInteger(Atom atom) {
        assertTrue(atom.isInteger(), "non-integer result in expression expecting integer");
    }
}
