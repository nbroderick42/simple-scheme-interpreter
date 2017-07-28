/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.lexer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author nick
 */
public class ReplLexer extends Lexer {

    protected ReplLexer() throws IOException {
        InputStream is = new BufferedInputStream(System.in);
        super.setInputStream(is);
    }
    
}
