package schemeinterpreter.parser;

import schemeinterpreter.lexer.FileLexer;

/**
 *
 * @author nick
 */
public class FileParser extends Parser {

    FileParser(FileLexer lexer) {
        super(lexer, PredictTable.makeFilePredictTable());
    }

}
