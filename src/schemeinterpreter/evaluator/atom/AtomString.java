/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator.atom;

import java.util.Objects;
import schemeinterpreter.evaluator.Evaluator;
import schemeinterpreter.parser.symbol.Symbol;
import schemeinterpreter.parser.symbol.SymbolString;

/**
 *
 * @author nick
 */
public class AtomString extends AtomImpl {
    
    private final java.lang.String val;

    public AtomString(java.lang.String val) {
        this.val = val;
    }

    public static AtomString make(SymbolString string) {
        return new AtomString(string.getValue());
    }

    public java.lang.String getValue() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AtomString) {
            return ((AtomString) o).getValue().equals(val);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.val);
        return hash;
    }

    @Override
    public Atom evaluate(Evaluator evaluator) {
        return evaluator.evaluate(this);
    }

    @Override
    public java.lang.String toString() {
        return val;
    }
    
}
