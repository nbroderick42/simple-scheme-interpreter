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
    
    public Symbol parse() throws IOException, SchemeInterpreterException {
        return parse(new Symbol.S());
    }
    
    private Symbol parse(Symbol currSymbol) throws IOException, SchemeInterpreterException {
        Token currToken = lexer.peek();
        ReplacementRule rule = predictTable.findRule(currSymbol, currToken.getType());
        List<Class<? extends Symbol>> replacements = rule.getRHS();

        for (Class<? extends Symbol> symbolType : replacements) {
            try {
                Symbol s = symbolType.newInstance();
                currSymbol.addChild(s);
                parse(s);
            }
            catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException("Couldn't create Symbol");
            }
        }
        
        if (currSymbol.isTerminal()) {
            currSymbol.acceptToken(currToken);
            lexer.next();
        }

        return currSymbol;
    }
    
}
