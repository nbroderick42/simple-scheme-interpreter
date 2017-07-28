/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import schemeinterpreter.lexer.FileLexer;
import schemeinterpreter.lexer.Lexer;
import schemeinterpreter.lexer.ReplLexer;
import schemeinterpreter.lexer.Token;

/**
 *
 * @author nick
 */
public abstract class Parser {
    
    public static Parser fromFile(File inputFile) throws IOException {
        FileLexer lexer = Lexer.makeFileLexer(inputFile);
        return new FileParser(lexer);
    }
    
    public static Parser fromStdin() throws IOException {
        ReplLexer lexer = Lexer.makeReplLexer();
        return new ReplParser(lexer);
    }
    
    private final Lexer lexer;
    
    private final PredictTable predictTable;
    
    protected Parser(Lexer lexer, PredictTable predictTable) {
        this.lexer = lexer;
        this.predictTable = predictTable;
    }

    public Symbol parse() throws SchemeFatalError {
        return parse(Symbol.makeStartSymbol());
    }

    protected Symbol parse(Symbol currentSymbol) throws SchemeFatalError {
        try {
            Token currentToken = lexer.peek();
            
            ReplacementRule rule = predictTable.findRule(currentSymbol, currentToken);
            List<Symbol> children = rule.apply(currentSymbol, this);
            
            children.forEach(currentSymbol::addChild);
            
            if (currentSymbol.isTerminal()) {
                currentToken = lexer.peek();
                currentSymbol.acceptToken(currentToken);
                
                if (currentSymbol.isTopLevelNewline()) {
                    lexer.reset();
                }
                else if (currentSymbol.isNewline()) {
                    lexer.reset();
                    lexer.nextToken();
                }
                else {
                    lexer.nextToken();
                }
            }
            
            return currentSymbol;
        }
        catch (IOException | NoSuchMethodException 
                | IllegalAccessException | InvocationTargetException | 
                  InstantiationException ex) 
        {
            throw new SchemeFatalError(ex.getMessage());
        }
    }

    public void reset() {
        lexer.reset();
    }
    
}
