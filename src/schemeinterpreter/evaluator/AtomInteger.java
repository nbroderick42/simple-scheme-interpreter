/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Objects;
import schemeinterpreter.evaluator.Evaluator;
import schemeinterpreter.parser.symbol.Symbol;
import schemeinterpreter.parser.symbol.SymbolInteger;

/**
 *
 * @author nick
 */
public class AtomInteger extends AtomImpl {

    private final java.lang.Integer val;

    public static AtomInteger make(SymbolInteger integer) {
        return new AtomInteger(integer.getValue());
    }

    public static AtomInteger make(java.lang.Integer integer) {
        return new AtomInteger(integer);
    }

    private AtomInteger(java.lang.Integer val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AtomInteger) {
            return ((AtomInteger) o).getValue().equals(val);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.val);
        return hash;
    }

    public java.lang.Integer getValue() {
        return val;
    }

    @Override
    public Atom evaluate() {
        return Evaluator.getInstance().evaluate(this);
    }

    @Override
    public java.lang.String toString() {
        return val.toString();
    }

}
