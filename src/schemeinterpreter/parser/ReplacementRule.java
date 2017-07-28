package schemeinterpreter.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import schemeinterpreter.evaluator.AtomBoolean;
import static java.util.stream.Collectors.toList;
import schemeinterpreter.evaluator.AtomList;
import schemeinterpreter.evaluator.AtomIdentifier;
import schemeinterpreter.evaluator.AtomInteger;
import schemeinterpreter.evaluator.AtomString;

/**
 *
 * @author nick
 */
@SuppressWarnings("unchecked")
public enum ReplacementRule {

    S_TO_EXPRS_EOF {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolExprs exprs = new SymbolExprs();
            SymbolEOF eof = new SymbolEOF();

            parser.parse(exprs);
            parser.parse(eof);

            parent.setEval(exprs.getEval());

            return asList(exprs, eof);
        }

    },
    S_TO_TOPLEVELEXPRS_TOPLEVELNL {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException 
        {
            SymbolTopLevelExprs topLevelExprs = new SymbolTopLevelExprs();
            SymbolTopLevelNewline topLevelNewline = new SymbolTopLevelNewline();

            parser.parse(topLevelExprs);
            parser.parse(topLevelNewline);

            parent.setEval(topLevelExprs.getEval());

            return asList(topLevelExprs, topLevelNewline);
        }

    },
    EXPRS_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) {
            parent.setEval(AtomList.makeEmptyList());
            return asList();
        }

    },
    EXPRS_TO_EXPR_EXPRS {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException 
        {
            SymbolExpr expr = new SymbolExpr();
            SymbolExprs exprs = new SymbolExprs();

            parser.parse(expr);
            parser.parse(exprs);

            AtomList exprs1Eval = (AtomList) exprs.getEval();
            exprs1Eval.prepend(expr.getEval());

            parent.setEval(exprs.getEval());

            return asList(expr, exprs);
        }

    },
    TOPLEVELEXPRS_TO_EXPR_TOPLEVELEXPRS {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            SymbolExpr expr = new SymbolExpr();
            SymbolTopLevelExprs topLevelExprs = new SymbolTopLevelExprs();

            parser.parse(expr);
            parser.parse(topLevelExprs);

            AtomList exprs1Eval = (AtomList) topLevelExprs.getEval();
            exprs1Eval.prepend(expr.getEval());

            parent.setEval(topLevelExprs.getEval());

            return asList(expr, topLevelExprs);
        }
    
    },
    EXPR_TO_NEWLINE_EXPR {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, InstantiationException, IllegalAccessException, 
                NoSuchMethodException, InvocationTargetException
        {
            SymbolNewline nl = new SymbolNewline();
            SymbolExpr expr = new SymbolExpr();
            
            parser.parse(nl);
            parser.parse(expr);
            
            parent.setEval(expr.getEval());
            
            return asList(nl, expr);
        }

    },
    EXPRS_TO_NEWLINE_EXPRS {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, InstantiationException, IllegalAccessException, 
                NoSuchMethodException, InvocationTargetException
        {
            SymbolNewline nl = new SymbolNewline();
            SymbolExprs exprs = new SymbolExprs();
            
            parser.parse(nl);
            parser.parse(exprs);
            
            parent.setEval(exprs.getEval());
            
            return asList(nl, exprs);
        }

    },
    EXPR_TO_QUOTE_EXPR {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolQuote quote = new SymbolQuote();
            SymbolExpr expr = new SymbolExpr();

            parser.parse(quote);
            parser.parse(expr);

            expr.getEval().setLazy(true);
            parent.setEval(expr.getEval());

            return asList(quote, expr);
        }

    },
    EXPR_TO_LIST {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolList list = new SymbolList();
            parser.parse(list);

            parent.setEval(list.getEval());

            return asList(list);
        }

    },
    EXPR_TO_IDENTIFIER {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolIdentifier identifier = new SymbolIdentifier();
            parser.parse(identifier);

            parent.setEval(AtomIdentifier.make(identifier));

            return asList(identifier);
        }

    },
    EXPR_TO_INTEGER {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolInteger integer = new SymbolInteger();
            parser.parse(integer);

            parent.setEval(AtomInteger.make(integer));

            return asList(integer);
        }

    },
    EXPR_TO_STRING {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolString string = new SymbolString();
            parser.parse(string);

            parent.setEval(AtomString.make(string));

            return asList(string);
        }

    },
    EXPR_TO_BOOLEAN {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolBoolean bool = new SymbolBoolean();
            parser.parse(bool);

            parent.setEval(AtomBoolean.make(bool));

            return asList(bool);
        }

    },
    LIST_TO_LPAREN_EXPRS_RPAREN {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolLparen lparen = new SymbolLparen();
            SymbolExprs exprs = new SymbolExprs();
            SymbolRparen rparen = new SymbolRparen();

            parser.parse(lparen);
            parser.parse(exprs);
            parser.parse(rparen);

            parent.setEval(exprs.getEval());

            return asList(lparen, exprs, rparen);
        }

    },
    TERMINAL_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            return asList();
        }

    };    

    public abstract List<Symbol> apply(Symbol parent, Parser parser)
            throws IOException,
            InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException;

    private static List<Symbol> asList(Symbol... symbols) {
        List<Symbol> result = Stream.of(symbols).collect(toList());
        return Collections.unmodifiableList(result);
    }

}
