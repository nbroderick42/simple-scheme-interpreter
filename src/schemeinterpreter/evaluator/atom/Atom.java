/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator.atom;

import schemeinterpreter.evaluator.Evaluator;
import static schemeinterpreter.evaluator.Evaluator.assertTrue;

/**
 *
 * @author nick
 */
public interface Atom {
    
    public boolean isLazy();
    
    public Atom evaluate(Evaluator evaluator);

    public default boolean isIdentifier() {
        return this instanceof AtomIdentifier;
    }

    public default boolean isString() {
        return this instanceof AtomString;
    }

    public default boolean isInteger() {
        return this instanceof AtomInteger;
    }

    public default boolean isVoid() {
        return this instanceof AtomVoid;
    }

    public default boolean isBoolean() {
        return this instanceof AtomBoolean;
    }

    public default boolean isList() {
        return this instanceof AtomList;
    }

    public default boolean isOperation() {
        return this instanceof AtomProcedure;
    }
    
    public default boolean isListPair() {
        if (this instanceof AtomList) {
            AtomList list = (AtomList) this;
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
