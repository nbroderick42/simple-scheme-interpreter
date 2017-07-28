package schemeinterpreter.parser;

import java.util.function.Consumer;
import schemeinterpreter.evaluator.Atom;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public abstract class Symbol {
    
    static Symbol makeStartSymbol() {
        return new SymbolS();
    }

    private Symbol nextSibling, firstChild, lastChild;

    private boolean terminal;

    private Atom eval;

    protected Symbol() {
        this.terminal = false;
    }

    void addChild(Symbol s) {
        if (firstChild == null) {
            firstChild = s;
        }
        else {
            lastChild.setNextSibling(s);
        }

        lastChild = s;
    }

    void setNextSibling(Symbol nextSibling) {
        this.nextSibling = nextSibling;
    }

    Symbol getFirstChild() {
        return firstChild;
    }

    Symbol getNextSibling() {
        return nextSibling;
    }

    void forEachChild(Consumer<Symbol> consumer) {
        for (Symbol curr = getFirstChild(); curr != null; curr = curr.getNextSibling()) {
            consumer.accept(curr);
        }
    }

    void acceptToken(Token token) {
    }

    boolean isTerminal() {
        return terminal;
    }

    String toFormattedString() {
        return toString();
    }

    void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public Atom getEval() {
        return eval;
    }

    void setEval(Atom eval) {
        this.eval = eval;
    }
    
    boolean isTopLevelNewline() {
        return this instanceof SymbolTopLevelNewline;
    }
    
    boolean isNewline() {
        return this instanceof SymbolNewline;
    }

}
