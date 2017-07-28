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
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public abstract class PredictTable {
     
    public static FilePredictTable makeFilePredictTable() {
        return new FilePredictTable();
    }
    
    public static ReplPredictTable makeReplPredictTable() {
        return new ReplPredictTable();
    }
    
    private final Map<Class<? extends Symbol>, Map<Class<? extends Token>, ReplacementRule>> table;
    
    protected PredictTable() {
        this.table = new HashMap<>();
    }
    
    public ReplacementRule findRule(Symbol symbol, Token token)
            throws SchemeSyntaxError, SchemeFatalError
    {
        Class<? extends Symbol> symbolType = symbol.getClass();
        Class<? extends Token> tokenType = token.getClass();
        
        if (!table.containsKey(symbolType)) {
            throw new SchemeFatalError("no predict table entry for " + symbolType);
        }
        else if (table.get(symbolType).containsKey(tokenType)) {
            return table.get(symbolType).get(tokenType);
        }
        else {
            throw makeMissingEntryException(symbolType, tokenType);
        }
    }

    private String expectedTokens(Class<? extends Symbol> sType) throws SchemeFatalError
    {
        List<Class<? extends Token>> tokenTypes = table.get(sType).keySet().stream()
                .collect(toList());
        List<String> tokenReprs = new ArrayList<>();

        for (Class<? extends Token> tokenType : tokenTypes) {
            try {
                String repr = (String) tokenType.getMethod("repr").invoke(null);
                tokenReprs.add(String.format("`%s`", repr));
            }
            catch (NoSuchMethodException | SecurityException | 
                    IllegalAccessException | IllegalArgumentException | 
                    InvocationTargetException ex) 
            {
                throw new SchemeFatalError(ex.getMessage());
            }
        }

        return tokenReprs.stream().collect(joining(", "));
    }
    
    
    private SchemeSyntaxError makeMissingEntryException(
            Class<? extends Symbol> sType, 
            Class<? extends Token> tType) throws SchemeSyntaxError, SchemeFatalError 
    {
        String expectedTokens = expectedTokens(sType);
        String actualToken = tType.getSimpleName();
        String message
                = String.format("encountered `%s`, "
                        + "expected one of %s.", actualToken, expectedTokens);

        return new SchemeSyntaxError(message);
    }
    
    protected void addEntry(Class<? extends Symbol> sType, Class<? extends Token> tType,
            ReplacementRule rule) {
        if (!table.containsKey(sType)) {
            table.put(sType, new HashMap<>());
        }

        table.get(sType).put(tType, rule);
    }
    
}
