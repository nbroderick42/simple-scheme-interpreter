/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.io.IOException;
import java.util.List;
import schemeinterpreter.SchemeInterpreterException;
import schemeinterpreter.lexer.Lexer;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public class Parser {
    
    private final Lexer lexer;
    private final PredictTable predictTable = PredictTable.makeTable();
    
    private Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    public static Parser fromLexer(Lexer lexer) {
        return new Parser(lexer);
    }
    
    public Symbol parse() throws IOException, SchemeInterpreterException,
                                 InstantiationException, IllegalAccessException
    {
        return parse(Symbol.getStartSymbol());
    }
    
    private Symbol parse(Symbol currSymbol) 
            throws IOException, SchemeInterpreterException, 
                   InstantiationException, IllegalAccessException
    {
        Token currToken = lexer.peek();
        
        Class<? extends Symbol> symType = currSymbol.getClass();
        Class<? extends Token> tokenType = currToken.getClass();
        
        ReplacementRule rule = predictTable.findRule(symType, tokenType);
        List<Class<? extends Symbol>> rhs = rule.getRHS();

        for (Class<? extends Symbol> sType : rhs) {
            Symbol sym = sType.newInstance();
            currSymbol.addChild(sym);
            parse(sym);
        }
        
        if (currSymbol.isTerminal()) {
            currSymbol.acceptToken(currToken);
            lexer.next();
        }

        return currSymbol;
    }
    
}
