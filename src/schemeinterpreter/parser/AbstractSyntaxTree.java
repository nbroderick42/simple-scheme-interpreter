/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import schemeinterpreter.SchemeInterpreterException;

/**
 *
 * @author nick
 */
public class AbstractSyntaxTree {
    
    private static final int SPACES_PER_INDENT = 2;
    
    private final Symbol root;
    
    private AbstractSyntaxTree(Symbol root) {
        this.root = root;
    }
    
    public static AbstractSyntaxTree buildTree(Parser parser) 
            throws IOException, SchemeInterpreterException,
                   InstantiationException, IllegalAccessException, 
                   NoSuchMethodException, InvocationTargetException 
    {
        return new AbstractSyntaxTree(parser.parse());
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
            int nextDepth = depth + (curr instanceof Symbol.ListExprs ? 1 : 0);
            curr.forEachChild(sym ->
                buildStringRepr(sb, sym, nextDepth)
            );
        }
    }
    
    private static String makeIndent(int depth) {
        return String.join("", Collections.nCopies(depth * SPACES_PER_INDENT, " "));
    }
    
    private static boolean isPrintableTerminal(Symbol s) {
        return s instanceof Symbol.Identifier ||
               s instanceof Symbol.Integer ||
               s instanceof Symbol.String ||
               s instanceof Symbol.Lparen || 
               s instanceof Symbol.Rparen;
    }

}
