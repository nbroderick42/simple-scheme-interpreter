/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Objects;
import schemeinterpreter.parser.symbol.SymbolBoolean;

/**
 *
 * @author nick
 */
public class AtomBoolean extends AtomImpl {

    private static final AtomBoolean FALSE = new AtomBoolean();
    private static final AtomBoolean TRUE = new AtomBoolean();

    public static AtomBoolean make(SymbolBoolean bool) {
        return bool.getValue() ? getTrue() : getFalse();
    }

    public static AtomBoolean make(boolean bool) {
        return bool ? getTrue() : getFalse();
    }
    
    public static AtomBoolean getTrue() {
        return TRUE;
    }
    
    public static AtomBoolean getFalse() {
        return FALSE;
    }

    public AtomBoolean negate() {
        return isFalse() ? getTrue() : getFalse();
    }

    @Override
    public Atom evaluate() {
        return Evaluator.evaluate(this);
    }

    public boolean isTrue() {
        return this == getTrue();
    }

    public boolean isFalse() {
        return this == getFalse();
    }


    @Override
    public String toString() {
        return isTrue() ? "#t" : "#f";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AtomBoolean) {
            AtomBoolean bool = (AtomBoolean) o;
            return (isTrue() && bool.isTrue()) || (isFalse() && bool.isFalse());
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (isTrue()) {
            return Objects.hashCode(true) ^ super.hashCode();
        }
        else {
            return Objects.hashCode(false) ^ super.hashCode();
        }
    }

}
