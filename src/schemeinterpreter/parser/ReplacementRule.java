/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;
import schemeinterpreter.SchemeInterpreterException;

/**
 *
 * @author nick
 */
@SuppressWarnings("unchecked")
public enum ReplacementRule {
    
    S_TO_EXPRS_EOF {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            // Symbol.S S = (Symbol.S) parent;
            
            Symbol.Exprs exprs = new Symbol.Exprs();
            Symbol.EOF eof = new Symbol.EOF();
            
            parser.parse(exprs);
            parser.parse(eof);            

            return asList(exprs, eof);
        }
        
    },

    EXPRS_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) {
            return asList();
        }
        
    },
    
    EXPRS_TO_EXPR_EXPRS {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Expr expr = new Symbol.Expr();
            Symbol.Exprs exprs = new Symbol.Exprs();
            
            parser.parse(expr);
            parser.parse(exprs);
            
            return asList(expr, exprs);
        }
        
    },
    
    EXPR_TO_QUOTE_EXPR {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Quote quote = new Symbol.Quote();
            Symbol.Expr expr = new Symbol.Expr();
            
            parser.parse(quote);
            parser.parse(expr);
            
            return asList(quote, expr);
        }
        
    },
    
    EXPR_TO_LIST {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.List list = new Symbol.List();
            parser.parse(list);
            
            return asList(list);
        }
        
    },
    
    EXPR_TO_IDENTIFIER {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Identifier identifier = new Symbol.Identifier();
            parser.parse(identifier);
            
            return asList(identifier);
        }
        
    },
    
    EXPR_TO_INTEGER {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Integer integer = new Symbol.Integer();
            parser.parse(integer);
            
            return asList(integer);
        }
        
    },
    
    EXPR_TO_STRING {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.String string = new Symbol.String();
            parser.parse(string);
            
            return asList(string);
        }
        
    },
    
    LIST_TO_LPAREN_LISTEXPRS_RPAREN {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Lparen lparen = new Symbol.Lparen();
            Symbol.ListExprs listExprs = new Symbol.ListExprs();
            Symbol.Rparen rparen = new Symbol.Rparen();
            
            parser.parse(lparen);
            parser.parse(listExprs);
            parser.parse(rparen);
            
            return asList(lparen, listExprs, rparen);
        }
        
    },
    
    LISTEXPR_TO_EXPRS {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            Symbol.Exprs exprs = new Symbol.Exprs();
            parser.parse(exprs);
            
            return asList(exprs);
        }
        
    },
    
    LPAREN_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    RPAREN_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    QUOTE_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    IDENTIFIER_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    INTEGER_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    STRING_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    },
    
    EOF_TO_EPSILON {
        
        @Override
        public List<Symbol> apply(Symbol parent, Parser parser) 
                throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
        {
            return asList();
        }
        
    };
    
    public abstract List<Symbol> apply(Symbol parent, Parser parser) 
            throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException;

    private static List<Symbol> asList(Symbol... symbols) {
        List<Symbol> result = Stream.of(symbols).collect(toList());
        return Collections.unmodifiableList(result);
    }
    
}
