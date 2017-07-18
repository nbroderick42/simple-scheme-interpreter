/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import schemeinterpreter.evaluator.EvaluationException;
import schemeinterpreter.evaluator.Evaluator;
import schemeinterpreter.lexer.Lexer;
import schemeinterpreter.parser.AbstractSyntaxTree;
import schemeinterpreter.parser.Parser;

/**
 *
 * @author nick
 */
public class SchemeInterpreter {
    
    public static void main(String[] args) throws Exception
    {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }
        
        Path pathToFile = Paths.get(args[0]);
        Lexer lexer = Lexer.scanFile(pathToFile);
        Parser parser = Parser.fromLexer(lexer);
        
        try {
            AbstractSyntaxTree ast = AbstractSyntaxTree.buildTree(parser);
            Evaluator.evaluate(ast);
        }
        catch (EvaluationException re) {
            System.out.println(re.getMessage());
        }

    }
    
    private static void printUsage() {
        System.out.println("Must supply filename of Scheme code to parse");
    }
    
}
