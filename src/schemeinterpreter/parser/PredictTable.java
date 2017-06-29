/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import schemeinterpreter.SchemeInterpreterException;
import schemeinterpreter.lexer.Token;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author nick
 */
public class PredictTable {
    
    private final Map<Class<? extends Symbol>, 
                  Map<Class<? extends Token>, ReplacementRule>> table;
    
    private PredictTable() {
        this.table = new HashMap<>();
        
        addSEntries();
        addExprsEntries();
        addExprEntries();
        addListEntries();
        addListExprsEntries();
        addTerminalEntries();
    }
    
    public static PredictTable makeTable() {
        return new PredictTable();
    }
    
    private void addSEntries() {
        addEntry(Symbol.S.class, Token.EOF.class, ReplacementRule.S_TO_EXPRS_EOF);
        addEntry(Symbol.S.class, Token.Quote.class, ReplacementRule.S_TO_EXPRS_EOF);
        addEntry(Symbol.S.class, Token.Identifier.class, ReplacementRule.S_TO_EXPRS_EOF);
        addEntry(Symbol.S.class, Token.Integer.class, ReplacementRule.S_TO_EXPRS_EOF);
        addEntry(Symbol.S.class, Token.String.class, ReplacementRule.S_TO_EXPRS_EOF);
        addEntry(Symbol.S.class, Token.Lparen.class, ReplacementRule.S_TO_EXPRS_EOF);
    }

    private void addExprsEntries() {        
        addEntry(Symbol.Exprs.class, Token.EOF.class, ReplacementRule.EXPRS_TO_EPSILON);
        addEntry(Symbol.Exprs.class, Token.Rparen.class, ReplacementRule.EXPRS_TO_EPSILON);
        
        addEntry(Symbol.Exprs.class, Token.Quote.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        addEntry(Symbol.Exprs.class, Token.Identifier.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        addEntry(Symbol.Exprs.class, Token.Integer.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        addEntry(Symbol.Exprs.class, Token.String.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        addEntry(Symbol.Exprs.class, Token.Lparen.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
    }

    private void addExprEntries() {        
        addEntry(Symbol.Expr.class, Token.Quote.class, ReplacementRule.EXPR_TO_QUOTE_EXPR);
        addEntry(Symbol.Expr.class, Token.Lparen.class, ReplacementRule.EXPR_TO_LIST);
        addEntry(Symbol.Expr.class, Token.Identifier.class, ReplacementRule.EXPR_TO_IDENTIFIER);
        addEntry(Symbol.Expr.class, Token.Integer.class, ReplacementRule.EXPR_TO_INTEGER);
        addEntry(Symbol.Expr.class, Token.String.class, ReplacementRule.EXPR_TO_STRING);
    }

    private void addListEntries() {
        addEntry(Symbol.List.class, Token.Lparen.class, ReplacementRule.LIST_TO_LPAREN_LISTEXPRS_RPAREN);
    }
    
    private void addListExprsEntries() {
        addEntry(Symbol.ListExprs.class, Token.Lparen.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        addEntry(Symbol.ListExprs.class, Token.Rparen.class, ReplacementRule.LISTEXPR_TO_EXPRS);        
        addEntry(Symbol.ListExprs.class, Token.Quote.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        addEntry(Symbol.ListExprs.class, Token.Identifier.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        addEntry(Symbol.ListExprs.class, Token.Integer.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        addEntry(Symbol.ListExprs.class, Token.String.class, ReplacementRule.LISTEXPR_TO_EXPRS);
    }
    
    private void addTerminalEntries() {
        addEntry(Symbol.Lparen.class, Token.Lparen.class, ReplacementRule.LPAREN_TO_EPSILON);
        addEntry(Symbol.Rparen.class, Token.Rparen.class, ReplacementRule.RPAREN_TO_EPSILON);
        addEntry(Symbol.Quote.class, Token.Quote.class, ReplacementRule.QUOTE_TO_EPSILON);
        addEntry(Symbol.Integer.class, Token.Integer.class, ReplacementRule.INTEGER_TO_EPSILON);
        addEntry(Symbol.Identifier.class, Token.Identifier.class, ReplacementRule.IDENTIFIER_TO_EPSILON);
        addEntry(Symbol.String.class, Token.String.class, ReplacementRule.STRING_TO_EPSILON);
        addEntry(Symbol.EOF.class, Token.EOF.class, ReplacementRule.EOF_TO_EPSILON);
    }
    
    public ReplacementRule findRule(Class<? extends Symbol> sType, Class<? extends Token> tType)
            throws SchemeInterpreterException, NoSuchMethodException,
                   IllegalAccessException, InvocationTargetException
    {        
        if (!table.containsKey(sType)) {
            throw new SchemeInterpreterException("No predict table entry for " + sType);
        }
        else if (table.get(sType).containsKey(tType)) {
            return table.get(sType).get(tType);
        }
        else {
            throw makeMissingEntryException(sType, tType);
        }
    }
    
    private String expectedTokens(Class<? extends Symbol> sType) 
            throws NoSuchMethodException, IllegalAccessException,
                   IllegalArgumentException, InvocationTargetException
    {
        List<Class<? extends Token>> tokenTypes = table.get(sType).keySet().stream()
                .collect(toList());
        
        List<String> tokenReprs = new ArrayList<>();
        
        for (Class<? extends Token> tokenType : tokenTypes) {
            String repr = (String) tokenType.getMethod("repr").invoke(null);
            tokenReprs.add(String.format("`%s`", repr));
        }
        
        return tokenReprs.stream().collect(joining(", "));
    }
    
    private SchemeInterpreterException makeMissingEntryException(
            Class<? extends Symbol> sType,
            Class<? extends Token> tType) 
                throws NoSuchMethodException, IllegalAccessException,
                       IllegalArgumentException, InvocationTargetException
    {
        String expectedTokens = expectedTokens(sType);
        String actualToken = tType.getSimpleName();
        String message = 
                String.format("Syntax error: encountered `%s`, "
                        + "expected one of %s.", actualToken, expectedTokens);

        return new SchemeInterpreterException(message);
    }
    
    private void addEntry(
            Class<? extends Symbol> sType, 
            Class<? extends Token> tType, 
            ReplacementRule rule)
    {
        if (!table.containsKey(sType)) {
            table.put(sType, new HashMap<>());
        }
        
        table.get(sType).put(tType, rule);
    }
 
}
