/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.parser;

import schemeinterpreter.lexer.TokenBoolean;
import schemeinterpreter.lexer.TokenEOF;
import schemeinterpreter.lexer.TokenIdentifier;
import schemeinterpreter.lexer.TokenInteger;
import schemeinterpreter.lexer.TokenLparen;
import schemeinterpreter.lexer.TokenNewline;
import schemeinterpreter.lexer.TokenQuote;
import schemeinterpreter.lexer.TokenRparen;
import schemeinterpreter.lexer.TokenString;
import static schemeinterpreter.parser.ReplacementRule.EXPRS_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.EXPRS_TO_EXPR_EXPRS;
import static schemeinterpreter.parser.ReplacementRule.EXPRS_TO_NEWLINE_EXPRS;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_BOOLEAN;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_IDENTIFIER;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_INTEGER;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_LIST;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_NEWLINE_EXPR;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_QUOTE_EXPR;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_STRING;
import static schemeinterpreter.parser.ReplacementRule.LIST_TO_LPAREN_EXPRS_RPAREN;
import static schemeinterpreter.parser.ReplacementRule.TERMINAL_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.TOPLEVELEXPRS_TO_EXPR_TOPLEVELEXPRS;
import static schemeinterpreter.parser.ReplacementRule.S_TO_TOPLEVELEXPRS_TOPLEVELNL;

/**
 *
 * @author nick
 */
public class ReplPredictTable extends PredictTable {
    
    protected ReplPredictTable() {
        addSEntries();
        addTopLevelExprsEntries();
        addExprsEntries();
        addExprEntries();
        addListEntries();
        addTerminalEntries();
    }
    
    private void addSEntries() {
        addEntry(SymbolS.class, TokenNewline.class, S_TO_TOPLEVELEXPRS_TOPLEVELNL);
        addEntry(SymbolS.class, TokenQuote.class, S_TO_TOPLEVELEXPRS_TOPLEVELNL);
        addEntry(SymbolS.class, TokenInteger.class, S_TO_TOPLEVELEXPRS_TOPLEVELNL);
        addEntry(SymbolS.class, TokenString.class, S_TO_TOPLEVELEXPRS_TOPLEVELNL);
        addEntry(SymbolS.class, TokenIdentifier.class, S_TO_TOPLEVELEXPRS_TOPLEVELNL);
        addEntry(SymbolS.class, TokenLparen.class, S_TO_TOPLEVELEXPRS_TOPLEVELNL);
        addEntry(SymbolS.class, TokenBoolean.class, S_TO_TOPLEVELEXPRS_TOPLEVELNL);
    }
    
    private void addTopLevelExprsEntries() {
        addEntry(SymbolTopLevelExprs.class, TokenRparen.class, EXPRS_TO_EPSILON);
        addEntry(SymbolTopLevelExprs.class, TokenNewline.class, EXPRS_TO_EPSILON);

        addEntry(SymbolTopLevelExprs.class, TokenIdentifier.class, TOPLEVELEXPRS_TO_EXPR_TOPLEVELEXPRS);
        addEntry(SymbolTopLevelExprs.class, TokenInteger.class, TOPLEVELEXPRS_TO_EXPR_TOPLEVELEXPRS);
        addEntry(SymbolTopLevelExprs.class, TokenString.class, TOPLEVELEXPRS_TO_EXPR_TOPLEVELEXPRS);
        addEntry(SymbolTopLevelExprs.class, TokenLparen.class, TOPLEVELEXPRS_TO_EXPR_TOPLEVELEXPRS);
        addEntry(SymbolTopLevelExprs.class, TokenBoolean.class, TOPLEVELEXPRS_TO_EXPR_TOPLEVELEXPRS);
    }
    
    private void addExprsEntries() {
        addEntry(SymbolExprs.class, TokenRparen.class, EXPRS_TO_EPSILON);
        
        addEntry(SymbolExprs.class, TokenNewline.class, EXPRS_TO_NEWLINE_EXPRS);
        
        addEntry(SymbolExprs.class, TokenIdentifier.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenInteger.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenString.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenLparen.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenBoolean.class, EXPRS_TO_EXPR_EXPRS);
    }
    
    private void addExprEntries() {
        addEntry(SymbolExpr.class, TokenQuote.class, EXPR_TO_QUOTE_EXPR);
        addEntry(SymbolExpr.class, TokenLparen.class, EXPR_TO_LIST);
        addEntry(SymbolExpr.class, TokenInteger.class, EXPR_TO_INTEGER);
        addEntry(SymbolExpr.class, TokenIdentifier.class, EXPR_TO_IDENTIFIER);
        addEntry(SymbolExpr.class, TokenString.class, EXPR_TO_STRING);
        addEntry(SymbolExpr.class, TokenBoolean.class, EXPR_TO_BOOLEAN);
        addEntry(SymbolExpr.class, TokenNewline.class, EXPR_TO_NEWLINE_EXPR);
    }
    
    private void addListEntries() {
        addEntry(SymbolList.class, TokenLparen.class, LIST_TO_LPAREN_EXPRS_RPAREN);
    }

    private void addTerminalEntries() {
        addEntry(SymbolLparen.class, TokenLparen.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolRparen.class, TokenRparen.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolQuote.class, TokenQuote.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolInteger.class, TokenInteger.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolIdentifier.class, TokenIdentifier.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolString.class, TokenString.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolEOF.class, TokenEOF.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolBoolean.class, TokenBoolean.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolNewline.class, TokenNewline.class, TERMINAL_TO_EPSILON);
        addEntry(SymbolTopLevelNewline.class, TokenNewline.class, TERMINAL_TO_EPSILON);
    }
    
}
