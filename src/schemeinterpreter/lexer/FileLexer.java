package schemeinterpreter.lexer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The main class used for lexical analysis of a Scheme program.
 *
 * @author nick
 */
public final class FileLexer extends Lexer {

    protected FileLexer(File inputFile) throws IOException {
        InputStream reader = new BufferedInputStream(new FileInputStream(inputFile));
        setInputStream(reader);
        scanNextChar();
    }
    
    @Override
    public Token nextToken() throws IOException {
        Token nextToken = super.nextToken();
        while (nextToken.isNewline()) {
            reset();
            nextToken = super.nextToken();
        }

        return nextToken;
    }

}
