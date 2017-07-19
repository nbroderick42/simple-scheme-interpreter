package schemeinterpreter.parser;

import schemeinterpreter.parser.symbol.SymbolString;
import schemeinterpreter.parser.symbol.SymbolIdentifier;
import schemeinterpreter.parser.symbol.SymbolListExprs;
import schemeinterpreter.parser.symbol.Symbol;
import schemeinterpreter.parser.symbol.SymbolLparen;
import schemeinterpreter.parser.symbol.SymbolInteger;
import schemeinterpreter.parser.symbol.SymbolRparen;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import static java.util.Collections.nCopies;

/**
 *
 * @author nick
 */
public class AbstractSyntaxTree {

    private static final int SPACES_PER_INDENT = 2;
    
    public static AbstractSyntaxTree buildTree(Parser parser)
            throws IOException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        return new AbstractSyntaxTree(parser.parse());
    }
    
    private static String makeIndent(int depth) {
        return String.join("", nCopies(depth * SPACES_PER_INDENT, " "));
    }
    
    private static boolean isPrintableTerminal(Symbol s) {
        return s instanceof SymbolIdentifier
                || s instanceof SymbolInteger
                || s instanceof SymbolString
                || s instanceof SymbolLparen
                || s instanceof SymbolRparen;
    }

    private final Symbol root;

    private AbstractSyntaxTree(Symbol root) {
        this.root = root;
    }


    public Symbol getRoot() {
        return root;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        buildStringRepr(sb, root, 0);

        return sb.toString();
    }

    private void buildStringRepr(StringBuilder sb, Symbol curr, int depth) {
        if (isPrintableTerminal(curr)) {
            String indent = makeIndent(depth);
            sb.append(indent).append(curr.toFormattedString()).append("\n");
        }
        else {
            int nextDepth = depth + (curr instanceof SymbolListExprs ? 1 : 0);
            curr.forEachChild(sym
                    -> buildStringRepr(sb, sym, nextDepth)
            );
        }
    }


}
