package schemeinterpreter.parser.symbol;

import java.util.function.Consumer;
import schemeinterpreter.evaluator.AtomImpl;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public abstract class Symbol {
    
    public static Symbol makeStartSymbol() {
        return new SymbolS();
    }

    private Symbol nextSibling, firstChild, lastChild;

    private boolean terminal;

    private AtomImpl eval;

    protected Symbol() {
        this.terminal = false;
    }

    public void addChild(Symbol s) {
        if (firstChild == null) {
            firstChild = s;
        }
        else {
            lastChild.setNextSibling(s);
        }

        lastChild = s;
    }

    public void setNextSibling(Symbol nextSibling) {
        this.nextSibling = nextSibling;
    }

    public Symbol getFirstChild() {
        return firstChild;
    }

    public Symbol getNextSibling() {
        return nextSibling;
    }

    public void forEachChild(Consumer<Symbol> consumer) {
        for (Symbol curr = getFirstChild(); curr != null; curr = curr.getNextSibling()) {
            consumer.accept(curr);
        }
    }

    public void acceptToken(Token token) {
    }

    public boolean isTerminal() {
        return terminal;
    }

    public String toFormattedString() {
        return toString();
    }

    /**
     * @param terminal the terminal to set
     */
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public AtomImpl getEval() {
        return eval;
    }

    public void setEval(AtomImpl eval) {
        this.eval = eval;
    }

    public boolean isEOF() {
        return this instanceof SymbolEOF;
    }

}
