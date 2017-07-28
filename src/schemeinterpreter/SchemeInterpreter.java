/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter;

import java.io.File;
import java.io.IOException;
import schemeinterpreter.evaluator.Evaluator;
import schemeinterpreter.evaluator.SchemeEvaluationError;

import schemeinterpreter.parser.Parser;
import schemeinterpreter.parser.SchemeFatalError;
import schemeinterpreter.parser.SchemeSyntaxError;
import schemeinterpreter.parser.Symbol;

/**
 *
 * @author nick
 */
public class SchemeInterpreter {

    public static void main(String[] args) throws IOException {
        try {
            switch (args.length) {
                case 0: 
                    beginRepl();
                    break;
                case 1:
                    beginFileEval(args[0]);
                    break;
                default:
                    printUsage();
            }
        }
        catch (SchemeFatalError ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void beginRepl() throws IOException {
        Parser parser = Parser.fromStdin();
        
        while (true) {
            try {
                Symbol astRoot = parser.parse();
                Evaluator.evaluate(astRoot);
            }
            catch (SchemeEvaluationError ex) {
                System.out.println(ex.getMessage());
            }
            catch (SchemeSyntaxError ex) {
                parser.reset();
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void beginFileEval(String inputFilePath) throws IOException {
        Parser parser = Parser.fromFile(new File(inputFilePath));
        Symbol astRoot = parser.parse();
        Evaluator.evaluate(astRoot);
    }

    private static void printUsage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
