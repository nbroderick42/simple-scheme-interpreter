/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nick
 */
public class Frame {
    
    public static Frame makeGlobalFrame() {
        return new Frame(null);
    }

    private Frame parent;

    private final Map<AtomIdentifier, Atom> table;

    private Frame(Frame parent) {
        this.parent = parent;
        this.table = new HashMap<>();
    }

    public void bind(AtomIdentifier id, Atom val) {
        table.put(id, val);
    }

    public void bind(String identifier, Atom val) {
        table.put(AtomIdentifier.make(identifier), val);
    }

    public void bind(Map.Entry<AtomIdentifier, Atom> entry) {
        table.put(entry.getKey(), entry.getValue());
    }

    public Atom resolve(AtomIdentifier id) {
        for (Frame curr = this; curr != null; curr = curr.getParent()) {
            if (curr.getTable().containsKey(id)) {
                return curr.getTable().get(id);
            }
        }
        throw new SchemeEvaluationError("`" + id + "' is not bound");
    }

    public boolean isBound(AtomIdentifier id) {
        return resolve(id) != null;
    }

    public Frame getParent() {
        return parent;
    }

    private Map<AtomIdentifier, Atom> getTable() {
        return Collections.unmodifiableMap(table);
    }

    public Frame attachNewFrame() {
        return new Frame(this);
    }

    public Frame detachFrame() {
        Frame parentFrame = this.parent;
        this.parent = null;
        return parentFrame;
    }

}
