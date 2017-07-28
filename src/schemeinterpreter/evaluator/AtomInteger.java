/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Objects;
import schemeinterpreter.parser.SymbolInteger;

/**
 *
 * @author nick
 */
public class AtomInteger extends AtomImpl {


    public static AtomInteger make(SymbolInteger integer) {
        return new AtomInteger(integer.getValue());
    }

    public static AtomInteger make(Integer integer) {
        return new AtomInteger(integer);
    }
    
    private final Integer val;

    private AtomInteger(Integer val) {
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

    public Integer getValue() {
        return val;
    }

    @Override
    public Atom evaluate() {
        return Evaluator.getInstance().evaluate(this);
    }

    @Override
    public String toString() {
        return val.toString();
    }

}
