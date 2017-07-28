/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Objects;
import schemeinterpreter.parser.SymbolIdentifier;

/**
 *
 * @author nick
 */
public class AtomIdentifier extends AtomImpl {

    public static AtomIdentifier make(SymbolIdentifier val) {
        return new AtomIdentifier(val.getValue());
    }

    public static AtomIdentifier make(String val) {
        return new AtomIdentifier(val);
    }
    
    private final String val;
    
    private AtomIdentifier(String val) {
        this.val = val;
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

    public String getValue() {
        return val;
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
