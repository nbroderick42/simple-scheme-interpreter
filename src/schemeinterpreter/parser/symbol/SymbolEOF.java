/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser.symbol;

/**
 *
 * @author nick
 */
public class SymbolEOF extends Symbol {
    
    public SymbolEOF() {
        super.setTerminal(true);
    }

    @Override
    public java.lang.String toString() {
        return "";
    }
    
}
