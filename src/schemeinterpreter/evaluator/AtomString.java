/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Objects;
import schemeinterpreter.evaluator.Evaluator;
import schemeinterpreter.parser.symbol.SymbolString;

/**
 *
 * @author nick
 */
public class AtomString extends AtomImpl {

    private final String val;

    public AtomString(String val) {
        this.val = val;
    }

    public static AtomString make(SymbolString string) {
        return new AtomString(string.getValue());
    }

    public String getValue() {
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
    public Atom evaluate() {
        return Evaluator.getInstance().evaluate(this);
    }

    @Override
    public String toString() {
        return val;
    }

}
