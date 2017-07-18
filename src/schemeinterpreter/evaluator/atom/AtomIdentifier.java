/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator.atom;

import java.util.Objects;
import schemeinterpreter.evaluator.Evaluator;
import schemeinterpreter.parser.symbol.Symbol;
import schemeinterpreter.parser.symbol.SymbolIdentifier;

/**
 *
 * @author nick
 */
public class AtomIdentifier extends AtomImpl {
    
    private final java.lang.String val;

    private AtomIdentifier(java.lang.String val) {
        this.val = val;
    }

    public static AtomIdentifier make(SymbolIdentifier val) {
        return new AtomIdentifier(val.getValue());
    }

    public static AtomIdentifier make(java.lang.String val) {
        return new AtomIdentifier(val);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AtomIdentifier) {
            return ((AtomIdentifier) o).getValue().equals(val);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.val);
        return hash;
    }

    public java.lang.String getValue() {
        return val;
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
