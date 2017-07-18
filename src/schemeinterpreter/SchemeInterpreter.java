/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter;

import static java.lang.System.exit;
import static java.lang.System.out;
import java.nio.file.Path;
import static java.nio.file.Paths.get;
import schemeinterpreter.evaluator.EvaluationException;
import schemeinterpreter.evaluator.Evaluator;
import schemeinterpreter.lexer.Lexer;
import static schemeinterpreter.lexer.Lexer.scanFile;
import schemeinterpreter.parser.AbstractSyntaxTree;
import static schemeinterpreter.parser.AbstractSyntaxTree.buildTree;
import schemeinterpreter.parser.Parser;
import static schemeinterpreter.parser.Parser.fromLexer;

/**
 *
 * @author nick
 */
public class SchemeInterpreter {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage();
            exit(1);
        }

        Path pathToFile = get(args[0]);
        Lexer lexer = scanFile(pathToFile);
        Parser parser = fromLexer(lexer);

        try {
            AbstractSyntaxTree ast = buildTree(parser);
            Evaluator.evaluate(ast);
        }
        catch (EvaluationException re) {
            out.println(re.getMessage());
        }

    }

    private static void printUsage() {
        out.println("Must supply filename of Scheme code to parse");
    }

}
