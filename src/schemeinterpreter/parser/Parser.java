package schemeinterpreter.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import schemeinterpreter.lexer.Lexer;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public class Parser {

    public static Parser fromLexer(Lexer lexer) {
        return new Parser(lexer);
    }

    private final Lexer lexer;
    private final PredictTable predictTable = PredictTable.makeTable();
    
    private Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    public Symbol parse() throws IOException,
                                 InstantiationException, IllegalAccessException,
                                 NoSuchMethodException, InvocationTargetException
    {
        return parse(Symbol.makeStartSymbol());
    }
    
    public Symbol parse(Symbol currSymbol) 
            throws IOException, 
                   InstantiationException, IllegalAccessException,
                   NoSuchMethodException, InvocationTargetException
    {   
        Class<? extends Symbol> symType = currSymbol.getClass();
        Class<? extends Token> tokenType = lexer.peek().getClass();
        
        ReplacementRule rule = predictTable.findRule(symType, tokenType);        
        List<Symbol> children = rule.apply(currSymbol, this);
        
        children.forEach(currSymbol::addChild);
        
        if (currSymbol.isTerminal()) {
            currSymbol.acceptToken(lexer.peek());
            lexer.next();
        }
        
        return currSymbol;
    }
    
}
