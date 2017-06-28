/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import static java.util.stream.Collectors.joining;
import schemeinterpreter.SchemeInterpreterException;
import schemeinterpreter.lexer.TokenType;

/**
 *
 * @author nick
 */
public class PredictTable {
    
    private final Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table;
    
    private PredictTable(Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table) {
        this.table = table;
    }
    
    public static PredictTable makeTable() {
        Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table = new HashMap<>();
        
        addEntries(table);
        
        return new PredictTable(table);
    }
    
    private static void addEntries(Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table) {
        addSEntries(table);
        addExprsEntries(table);
        addExprEntries(table);
        addListEntries(table);
        addListExprEntries(table);
        addTerminalEntries(table);
    }
    
    private static void addSEntries(Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table) {
        Map<TokenType, ReplacementRule> entry = new EnumMap<>(TokenType.class);
        table.put(Symbol.S.class, entry);
        
        entry.put(TokenType.EOF, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(TokenType.QUOTE, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(TokenType.IDENTIFIER, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(TokenType.INTEGER, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(TokenType.STRING, ReplacementRule.S_TO_EXPRS_EOF);
        entry.put(TokenType.LPAREN, ReplacementRule.S_TO_EXPRS_EOF);
    }

    private static void addExprsEntries(Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table) {
        Map<TokenType, ReplacementRule> entry = new EnumMap<>(TokenType.class);
        table.put(Symbol.Exprs.class, entry);
        
        entry.put(TokenType.EOF, ReplacementRule.EXPRS_TO_EPSILON);
        entry.put(TokenType.RPAREN, ReplacementRule.EXPRS_TO_EPSILON);
        
        entry.put(TokenType.QUOTE, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        entry.put(TokenType.IDENTIFIER, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        entry.put(TokenType.INTEGER, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        entry.put(TokenType.STRING, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
        entry.put(TokenType.LPAREN, ReplacementRule.EXPRS_TO_EXPR_EXPRS);
    }

    private static void addExprEntries(Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table) {
        Map<TokenType, ReplacementRule> entry = new EnumMap<>(TokenType.class);
        table.put(Symbol.Expr.class, entry);
        
        entry.put(TokenType.QUOTE, ReplacementRule.EXPR_TO_QUOTE_EXPR);
        entry.put(TokenType.LPAREN, ReplacementRule.EXPR_TO_LIST);
        entry.put(TokenType.IDENTIFIER, ReplacementRule.EXPR_TO_IDENTIFIER);
        entry.put(TokenType.INTEGER, ReplacementRule.EXPR_TO_INTEGER);
        entry.put(TokenType.STRING, ReplacementRule.EXPR_TO_STRING);
    }

    private static void addListEntries(Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table) {
        Map<TokenType, ReplacementRule> entry = new EnumMap<>(TokenType.class);
        table.put(Symbol.List.class, entry);
        
        entry.put(TokenType.LPAREN, ReplacementRule.LIST_TO_LPAREN_LISTEXPRS_RPAREN);
    }
    
    private static void addListExprEntries(Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table) {
        Map<TokenType, ReplacementRule> entry = new EnumMap<>(TokenType.class);
        table.put(Symbol.ListExprs.class, entry);
        
        entry.put(TokenType.EOF, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(TokenType.RPAREN, ReplacementRule.LISTEXPR_TO_EXPRS);        
        entry.put(TokenType.QUOTE, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(TokenType.IDENTIFIER, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(TokenType.INTEGER, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(TokenType.STRING, ReplacementRule.LISTEXPR_TO_EXPRS);
        entry.put(TokenType.LPAREN, ReplacementRule.LISTEXPR_TO_EXPRS);
    }
    
    private static void addTerminalEntries(Map<Class<? extends Symbol>, Map<TokenType, ReplacementRule>> table) {
        Map<TokenType, ReplacementRule> lparenEntry = new EnumMap<>(TokenType.class);
        table.put(Symbol.Lparen.class, lparenEntry);
        lparenEntry.put(TokenType.LPAREN, ReplacementRule.LPAREN_TO_EPSILON);
        
        Map<TokenType, ReplacementRule> rparenEntry = new EnumMap<>(TokenType.class);
        table.put(Symbol.Rparen.class, rparenEntry);
        rparenEntry.put(TokenType.RPAREN, ReplacementRule.RPAREN_TO_EPSILON);
        
        Map<TokenType, ReplacementRule> quoteEntry = new EnumMap<>(TokenType.class);
        table.put(Symbol.Quote.class, quoteEntry);
        quoteEntry.put(TokenType.QUOTE, ReplacementRule.QUOTE_TO_EPSILON);
        
        Map<TokenType, ReplacementRule> integerEntry = new EnumMap<>(TokenType.class);
        table.put(Symbol.Integer.class, integerEntry);
        integerEntry.put(TokenType.INTEGER, ReplacementRule.INTEGER_TO_EPSILON);
        
        Map<TokenType, ReplacementRule> identifierEntry = new EnumMap<>(TokenType.class);
        table.put(Symbol.Identifier.class, identifierEntry);
        identifierEntry.put(TokenType.IDENTIFIER, ReplacementRule.IDENTIFIER_TO_EPSILON);
        
        Map<TokenType, ReplacementRule> stringEntry = new EnumMap<>(TokenType.class);
        table.put(Symbol.String.class, stringEntry);
        stringEntry.put(TokenType.STRING, ReplacementRule.STRING_TO_EPSILON);

        Map<TokenType, ReplacementRule> eofEntry = new EnumMap<>(TokenType.class);
        table.put(Symbol.EOF.class, eofEntry);
        eofEntry.put(TokenType.EOF, ReplacementRule.EOF_TO_EPSILON);
    }
    
    public ReplacementRule findRule(Symbol sym, TokenType tType) 
            throws SchemeInterpreterException 
    {
        Class<? extends Symbol> sType = sym.getClass();
        
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
    
    private String expectedTokenTypes(Class<? extends Symbol> sType) {
        return table.get(sType)
                .keySet()
                .stream()
                .map(TokenType::toString)
                .collect(joining(", ", "`", "`"));
    }
    
    private SchemeInterpreterException missingEntryException(
            Class<? extends Symbol> sType,
            TokenType actualTokenType
    ) {
        String expectedTokens = expectedTokenTypes(sType);
        String actualToken = actualTokenType.toString();
        String message = 
                String.format("Syntax error: encountered `%s`, expected one of %s.", actualToken, expectedTokens);

        return new SchemeInterpreterException(message);
    }
 
}
