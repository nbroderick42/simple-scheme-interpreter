/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

/**
 *
 * @author nick
 */
public class SymbolTopLevelNewline extends Symbol {

    public SymbolTopLevelNewline() {
        super.setTerminal(true);
    }
    
    @Override
    public String toString() {
        return "[toplevelnewline]";
    }
    
}
