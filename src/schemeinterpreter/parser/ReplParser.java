/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import schemeinterpreter.lexer.Lexer;

/**
 *
 * @author nick
 */
public class ReplParser extends Parser {

    ReplParser(Lexer lexer) {
        super(lexer, PredictTable.makeReplPredictTable());
    }

}
