/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.util.HashMap;
import java.util.Map;
import static java.util.stream.Collectors.joining;
import schemeinterpreter.SchemeInterpreterException;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public class PredictTable {
    
    private final Map<Class<? extends Symbol>, 
                  Map<Class<? extends Token>, ReplacementRule>> table;
    
    private PredictTable(
            Map<Class<? extends Symbol>, 
            Map<Class<? extends Token>, ReplacementRule>> table)
    {
        this.table = table;
    }
    
    public static PredictTable makeTable() {
        Map<Class<? extends Symbol>,
        Map<Class<? extends Token>, ReplacementRule>> table = new HashMap<>();
        
        addEntries(table);
        
        return new PredictTable(table);
    }
    
    private static void addEntries(
            Map<Class<? extends Symbol>, 
            Map<Class<? extends Token>, ReplacementRule>> table)
    {
        addSEntries(table);
        addExprsEntries(table);
        addExprEntries(table);
        addListEntries(table);
        addListExprEntries(table);
        addTerminalEntries(table);
    }
    
    private static void addSEntries(
            Map<Class<? extends Symbol>, 
            Map<Class<? extends Token>, ReplacementRule>> table)
    {
        Map<Class<? extends Token>, ReplacementRule> entry = new HashMap<>();
        table.put(Symbol.S.class, entry);
        
        entry.put(Token.EOF.class, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(Token.Quote.class, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(Token.Identifier.class, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(Token.Integer.class, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(Token.String.class, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(Token.Lparen.class, ReplacementRule.S_TO_EXPRS_EOF);
    }

    private static void addExprsEntries(
            Map<Class<? extends Symbol>, 
            Map<Class<? extends Token>, ReplacementRule>> table)
    {
        Map<Class<? extends Token>, ReplacementRule> entry = new HashMap<>();
        table.put(Symbol.Exprs.class, entry);
        
        entry.put(Token.EOF.class, ReplacementRule.EXPRS_TO_EPSILON);
        entry.put(Token.Rparen.class, ReplacementRule.EXPRS_TO_EPSILON);
        
        entry.put(Token.Quote.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        entry.put(Token.Identifier.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        entry.put(Token.Integer.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        entry.put(Token.String.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        entry.put(Token.Lparen.class, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
    }

    private static void addExprEntries(
            Map<Class<? extends Symbol>, 
            Map<Class<? extends Token>, ReplacementRule>> table)
    {
        Map<Class<? extends Token>, ReplacementRule> entry = new HashMap<>();
        table.put(Symbol.Expr.class, entry);
        
        entry.put(Token.Quote.class, ReplacementRule.EXPR_TO_QUOTE_EXPR);
        entry.put(Token.Lparen.class, ReplacementRule.EXPR_TO_LIST);
        entry.put(Token.Identifier.class, ReplacementRule.EXPR_TO_IDENTIFIER);
        entry.put(Token.Integer.class, ReplacementRule.EXPR_TO_INTEGER);
        entry.put(Token.String.class, ReplacementRule.EXPR_TO_STRING);
    }

    private static void addListEntries(
            Map<Class<? extends Symbol>, 
            Map<Class<? extends Token>, ReplacementRule>> table)
    {
        Map<Class<? extends Token>, ReplacementRule> entry = new HashMap<>();
        table.put(Symbol.List.class, entry);
        
        entry.put(Token.Lparen.class, ReplacementRule.LIST_TO_LPAREN_LISTEXPRS_RPAREN);
    }
    
    private static void addListExprEntries(
            Map<Class<? extends Symbol>, 
            Map<Class<? extends Token>, ReplacementRule>> table)
    {
        Map<Class<? extends Token>, ReplacementRule> entry = new HashMap<>();
        table.put(Symbol.ListExprs.class, entry);
        
        entry.put(Token.EOF.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(Token.Rparen.class, ReplacementRule.LISTEXPR_TO_EXPRS);        
        entry.put(Token.Quote.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(Token.Identifier.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(Token.Integer.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(Token.String.class, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(Token.Lparen.class, ReplacementRule.LISTEXPR_TO_EXPRS);
    }
    
    private static void addTerminalEntries(
            Map<Class<? extends Symbol>, 
            Map<Class<? extends Token>, ReplacementRule>> table)
    {
        Map<Class<? extends Token>, ReplacementRule> lparenEntry = new HashMap<>();
        table.put(Symbol.Lparen.class, lparenEntry);
        lparenEntry.put(Token.Lparen.class, ReplacementRule.LPAREN_TO_EPSILON);
        
        Map<Class<? extends Token>, ReplacementRule> rparenEntry = new HashMap<>();
        table.put(Symbol.Rparen.class, rparenEntry);
        rparenEntry.put(Token.Rparen.class, ReplacementRule.RPAREN_TO_EPSILON);
        
        Map<Class<? extends Token>, ReplacementRule> quoteEntry = new HashMap<>();
        table.put(Symbol.Quote.class, quoteEntry);
        quoteEntry.put(Token.Quote.class, ReplacementRule.QUOTE_TO_EPSILON);
        
        Map<Class<? extends Token>, ReplacementRule> integerEntry = new HashMap<>();
        table.put(Symbol.Integer.class, integerEntry);
        integerEntry.put(Token.Integer.class, ReplacementRule.INTEGER_TO_EPSILON);
        
        Map<Class<? extends Token>, ReplacementRule> identifierEntry = new HashMap<>();
        table.put(Symbol.Identifier.class, identifierEntry);
        identifierEntry.put(Token.Identifier.class, ReplacementRule.IDENTIFIER_TO_EPSILON);
        
        Map<Class<? extends Token>, ReplacementRule> stringEntry = new HashMap<>();
        table.put(Symbol.String.class, stringEntry);
        stringEntry.put(Token.String.class, ReplacementRule.STRING_TO_EPSILON);

        Map<Class<? extends Token>, ReplacementRule> eofEntry = new HashMap<>();
        table.put(Symbol.EOF.class, eofEntry);
        eofEntry.put(Token.EOF.class, ReplacementRule.EOF_TO_EPSILON);
    }
    
    public ReplacementRule findRule(
            Class<? extends Symbol> sType, 
            Class<? extends Token> tType) throws SchemeInterpreterException 
    {        
        if (!table.containsKey(sType)) {
            throw new SchemeInterpreterException("No predict table entry for " + sType);
        }
        else if (table.get(sType).containsKey(tType)) {
            return table.get(sType).get(tType);
        }
        else {
            throw missingEntryException(sType, tType);
        }
    }
    
    private String expectedTokens(Class<? extends Symbol> sType) {
        return table.get(sType)
                .keySet()
                .stream()
                .map(Class::getSimpleName)
                .collect(joining(", ", "`", "`"));
    }
    
    private SchemeInterpreterException missingEntryException(
            Class<? extends Symbol> sType,
            Class<? extends Token> tType
    ) {
        String expectedTokens = expectedTokens(sType);
        String actualToken = tType.getSimpleName();
        String message = 
                String.format("Syntax error: encountered `%s`, "
                        + "expected one of %s.", actualToken, expectedTokens);

        return new SchemeInterpreterException(message);
    }
 
}
