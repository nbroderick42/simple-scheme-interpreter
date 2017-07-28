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

    void setLazy(boolean lazy);
    
    boolean isLazy();

    Atom evaluate();

    default boolean isIdentifier() {
        return this instanceof AtomIdentifier;
    }

    default boolean isString() {
        return this instanceof AtomString;
    }

    default boolean isInteger() {
        return this instanceof AtomInteger;
    }

    default boolean isVoid() {
        return this instanceof AtomVoid;
    }

    default boolean isBoolean() {
        return this instanceof AtomBoolean;
    }

    default boolean isList() {
        return this instanceof AtomList;
    }

    default boolean isProcedure() {
        return this instanceof AtomProcedure;
    }

    default boolean isListPair() {
        if (this instanceof AtomList) {
            AtomList list = (AtomList) this;
            return list.isPair();
        }

        return false;
    }

    static void assertIsBoolean(Atom atom) {
        assertTrue(atom.isBoolean(), "non-boolean result in expression expecting boolean");
    }

    static void assertIsInteger(Atom atom) {
        assertTrue(atom.isInteger(), "non-integer result in expression expecting integer");
    }
}
