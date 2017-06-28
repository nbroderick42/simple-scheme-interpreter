/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter;

import java.io.IOException;
import java.nio.file.Paths;
import schemeinterpreter.lexer.Lexer;
import schemeinterpreter.parser.AbstractSyntaxTree;
import schemeinterpreter.parser.Parser;

/**
 *
 * @author nick
 */
public class SchemeInterpreter {

    public static void main(String[] args) throws IOException, SchemeInterpreterException {
        Lexer lex = Lexer.scanFile(Paths.get("testfile"));
        Parser parser = Parser.fromLexer(lex);
        AbstractSyntaxTree ast = AbstractSyntaxTree.buildTree(parser);
        
        System.out.println(ast);
    }

}
