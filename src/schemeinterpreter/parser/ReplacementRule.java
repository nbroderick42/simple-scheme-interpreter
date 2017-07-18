package schemeinterpreter.parser;

import schemeinterpreter.parser.symbol.SymbolString;
import schemeinterpreter.parser.symbol.SymbolQuote;
import schemeinterpreter.parser.symbol.SymbolListExprs;
import schemeinterpreter.parser.symbol.SymbolExprs;
import schemeinterpreter.parser.symbol.SymbolLparen;
import schemeinterpreter.parser.symbol.SymbolRparen;
import schemeinterpreter.parser.symbol.SymbolIdentifier;
import schemeinterpreter.parser.symbol.SymbolList;
import schemeinterpreter.parser.symbol.SymbolExpr;
import schemeinterpreter.parser.symbol.SymbolEOF;
import schemeinterpreter.parser.symbol.Symbol;
import schemeinterpreter.parser.symbol.SymbolBoolean;
import schemeinterpreter.parser.symbol.SymbolInteger;
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
                NoSuchMethodException, InvocationTargetException {
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
    LIST_TO_LPAREN_LISTEXPRS_RPAREN {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolLparen lparen = new SymbolLparen();
            SymbolListExprs listExprs = new SymbolListExprs();
            SymbolRparen rparen = new SymbolRparen();

            parser.parse(lparen);
            parser.parse(listExprs);
            parser.parse(rparen);

            parent.setEval(listExprs.getEval());

            return asList(lparen, listExprs, rparen);
        }

    },
    LISTEXPR_TO_EXPRS {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            SymbolExprs exprs = new SymbolExprs();
            parser.parse(exprs);

            parent.setEval(exprs.getEval());

            return asList(exprs);
        }

    },
    LPAREN_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            return asList();
        }

    },
    RPAREN_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            return asList();
        }

    },
    QUOTE_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            return asList();
        }

    },
    IDENTIFIER_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            return asList();
        }

    },
    INTEGER_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            return asList();
        }

    },
    STRING_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            return asList();
        }

    },
    BOOLEAN_TO_EPSILON {

        @Override
        public List<Symbol> apply(Symbol parent, Parser parser)
                throws IOException,
                InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            return asList();
        }

    },
    EOF_TO_EPSILON {

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
