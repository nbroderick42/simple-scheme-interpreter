package schemeinterpreter.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;
import schemeinterpreter.evaluator.AtomImpl;

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
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Exprs exprs = new Symbol.Exprs();
            Symbol.EOF eof = new Symbol.EOF();
            
            parser.parse(exprs);
            parser.parse(eof);
            
            parent.setEval(exprs.getEval());

            return asList(exprs, eof);
        }
        
    },

    EXPRS_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) {
            parent.setEval(AtomImpl.List.makeEmptyList());
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
            Symbol.Expr expr = new Symbol.Expr();
            Symbol.Exprs exprs = new Symbol.Exprs();
            
            parser.parse(expr);
            parser.parse(exprs);
            
            AtomImpl.List exprs1Eval = (AtomImpl.List) exprs.getEval();
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
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Quote quote = new Symbol.Quote();
            Symbol.Expr expr = new Symbol.Expr();
            
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
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.List list = new Symbol.List();
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
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Identifier identifier = new Symbol.Identifier();
            parser.parse(identifier);
            
            parent.setEval(AtomImpl.Identifier.make(identifier));
            
            return asList(identifier);
        }
        
    },
    
    EXPR_TO_INTEGER {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Integer integer = new Symbol.Integer();
            parser.parse(integer);
            
            parent.setEval(AtomImpl.Integer.make(integer));
            
            return asList(integer);
        }
        
    },
    
    EXPR_TO_STRING {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.String string = new Symbol.String();
            parser.parse(string);
            
            parent.setEval(AtomImpl.String.make(string));
            
            return asList(string);
        }
        
    },
    
    EXPR_TO_BOOLEAN {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Boolean bool = new Symbol.Boolean();
            parser.parse(bool);
            
            parent.setEval(AtomImpl.Boolean.make(bool));
            
            return asList(bool);
        }
        
    },
    
    LIST_TO_LPAREN_LISTEXPRS_RPAREN {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Lparen lparen = new Symbol.Lparen();
            Symbol.ListExprs listExprs = new Symbol.ListExprs();
            Symbol.Rparen rparen = new Symbol.Rparen();
            
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
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Exprs exprs = new Symbol.Exprs();
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
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    RPAREN_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    QUOTE_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    IDENTIFIER_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    INTEGER_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    STRING_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    BOOLEAN_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    EOF_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException,
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
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
