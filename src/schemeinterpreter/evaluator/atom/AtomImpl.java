/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator.atom;

/**
 *
 * @author nick
 */

public abstract class AtomImpl implements Atom {
    
    private boolean lazy;
    
    @Override
    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

}
