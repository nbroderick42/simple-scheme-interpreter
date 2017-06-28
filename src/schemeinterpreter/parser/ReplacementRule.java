/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author nick
 */
@SuppressWarnings("unchecked")
public enum ReplacementRule {
    
    S_TO_EXPRS_EOF(Symbol.S.class, Symbol.Exprs.class, Symbol.EOF.class),
    
    EXPRS_TO_EPSILON(Symbol.Exprs.class),
    
    EXPRS_TO_EXPR_EXPRS(Symbol.Exprs.class, Symbol.Expr.class, Symbol.Exprs.class),
    
    EXPR_TO_QUOTE_EXPR(Symbol.Quote.class, Symbol.Exprs.class),
    
    EXPR_TO_LIST(Symbol.Expr.class, Symbol.List.class),
    
    EXPR_TO_IDENTIFIER(Symbol.Expr.class, Symbol.Identifier.class),
    
    EXPR_TO_INTEGER(Symbol.Expr.class, Symbol.Integer.class),
    
    EXPR_TO_STRING(Symbol.Expr.class, Symbol.String.class),
    
    LIST_TO_LPAREN_LISTEXPRS_RPAREN(Symbol.List.class, Symbol.Lparen.class, Symbol.ListExprs.class, Symbol.Rparen.class),
    
    LISTEXPR_TO_EXPRS(Symbol.ListExprs.class, Symbol.Exprs.class),
    
    LPAREN_TO_EPSILON(Symbol.Lparen.class),
    
    RPAREN_TO_EPSILON(Symbol.Rparen.class),
    
    QUOTE_TO_EPSILON(Symbol.Quote.class),
    
    IDENTIFIER_TO_EPSILON(Symbol.Identifier.class),
    
    INTEGER_TO_EPSILON(Symbol.Integer.class),
    
    STRING_TO_EPSILON(Symbol.String.class),
    
    EOF_TO_EPSILON(Symbol.EOF.class);
    
    private final Class<? extends Symbol> lhs;
    private final List<Class<? extends Symbol>> rhs;
    
    private ReplacementRule(Class<? extends Symbol> lhs, Class<? extends Symbol>... rhs) {
        this.lhs = lhs;
        this.rhs = Arrays.asList(rhs);
    }
    
    public Class<? extends Symbol> getLHS() {
        return lhs;
    }
    
    public List<Class<? extends Symbol>> getRHS() {
        return Collections.unmodifiableList(rhs);
    }
    
}
