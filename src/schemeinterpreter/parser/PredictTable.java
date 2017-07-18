package schemeinterpreter.parser;

import static java.lang.String.format;
import schemeinterpreter.parser.symbol.SymbolString;
import schemeinterpreter.parser.symbol.SymbolQuote;
import schemeinterpreter.parser.symbol.SymbolListExprs;
import schemeinterpreter.parser.symbol.SymbolExprs;
import schemeinterpreter.parser.symbol.SymbolLparen;
import schemeinterpreter.parser.symbol.SymbolRparen;
import schemeinterpreter.parser.symbol.SymbolIdentifier;
import schemeinterpreter.parser.symbol.SymbolEOF;
import schemeinterpreter.parser.symbol.SymbolList;
import schemeinterpreter.parser.symbol.SymbolExpr;
import schemeinterpreter.parser.symbol.SymbolS;
import schemeinterpreter.parser.symbol.Symbol;
import schemeinterpreter.parser.symbol.SymbolBoolean;
import schemeinterpreter.parser.symbol.SymbolInteger;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import schemeinterpreter.SchemeInterpreterException;
import schemeinterpreter.lexer.token.Token;
import schemeinterpreter.lexer.token.TokenBoolean;
import schemeinterpreter.lexer.token.TokenEOF;
import schemeinterpreter.lexer.token.TokenIdentifier;
import schemeinterpreter.lexer.token.TokenInteger;
import schemeinterpreter.lexer.token.TokenLparen;
import schemeinterpreter.lexer.token.TokenQuote;
import schemeinterpreter.lexer.token.TokenRparen;
import schemeinterpreter.lexer.token.TokenString;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static schemeinterpreter.parser.ReplacementRule.BOOLEAN_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.EOF_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.EXPRS_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.EXPRS_TO_EXPR_EXPRS;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_BOOLEAN;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_IDENTIFIER;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_INTEGER;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_LIST;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_QUOTE_EXPR;
import static schemeinterpreter.parser.ReplacementRule.EXPR_TO_STRING;
import static schemeinterpreter.parser.ReplacementRule.IDENTIFIER_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.INTEGER_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.LISTEXPR_TO_EXPRS;
import static schemeinterpreter.parser.ReplacementRule.LIST_TO_LPAREN_LISTEXPRS_RPAREN;
import static schemeinterpreter.parser.ReplacementRule.LPAREN_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.QUOTE_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.RPAREN_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.STRING_TO_EPSILON;
import static schemeinterpreter.parser.ReplacementRule.S_TO_EXPRS_EOF;

/**
 *
 * @author nick
 */
public class PredictTable {

    private final Map<Class<? extends Symbol>, Map<Class<? extends Token>, ReplacementRule>> table;

    private PredictTable() {
        this.table = new HashMap<>();

        addSEntries();
        addExprsEntries();
        addExprEntries();
        addListEntries();
        addListExprsEntries();
        addTerminalEntries();
    }

    public static PredictTable makeTable() {
        return new PredictTable();
    }

    private void addSEntries() {
        addEntry(SymbolS.class, TokenEOF.class, S_TO_EXPRS_EOF);
        addEntry(SymbolS.class, TokenQuote.class, S_TO_EXPRS_EOF);
        addEntry(SymbolS.class, TokenIdentifier.class, S_TO_EXPRS_EOF);
        addEntry(SymbolS.class, TokenInteger.class, S_TO_EXPRS_EOF);
        addEntry(SymbolS.class, TokenString.class, S_TO_EXPRS_EOF);
        addEntry(SymbolS.class, TokenLparen.class, S_TO_EXPRS_EOF);
        addEntry(SymbolS.class, TokenBoolean.class, S_TO_EXPRS_EOF);
    }

    private void addExprsEntries() {
        addEntry(SymbolExprs.class, TokenEOF.class, EXPRS_TO_EPSILON);
        addEntry(SymbolExprs.class, TokenRparen.class, EXPRS_TO_EPSILON);

        addEntry(SymbolExprs.class, TokenQuote.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenIdentifier.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenInteger.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenString.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenLparen.class, EXPRS_TO_EXPR_EXPRS);
        addEntry(SymbolExprs.class, TokenBoolean.class, EXPRS_TO_EXPR_EXPRS);
    }

    private void addExprEntries() {
        addEntry(SymbolExpr.class, TokenQuote.class, EXPR_TO_QUOTE_EXPR);
        addEntry(SymbolExpr.class, TokenLparen.class, EXPR_TO_LIST);
        addEntry(SymbolExpr.class, TokenIdentifier.class, EXPR_TO_IDENTIFIER);
        addEntry(SymbolExpr.class, TokenInteger.class, EXPR_TO_INTEGER);
        addEntry(SymbolExpr.class, TokenString.class, EXPR_TO_STRING);
        addEntry(SymbolExpr.class, TokenBoolean.class, EXPR_TO_BOOLEAN);
    }

    private void addListEntries() {
        addEntry(SymbolList.class, TokenLparen.class, LIST_TO_LPAREN_LISTEXPRS_RPAREN);
    }

    private void addListExprsEntries() {
        addEntry(SymbolListExprs.class, TokenLparen.class, LISTEXPR_TO_EXPRS);
        addEntry(SymbolListExprs.class, TokenRparen.class, LISTEXPR_TO_EXPRS);
        addEntry(SymbolListExprs.class, TokenQuote.class, LISTEXPR_TO_EXPRS);
        addEntry(SymbolListExprs.class, TokenIdentifier.class, LISTEXPR_TO_EXPRS);
        addEntry(SymbolListExprs.class, TokenInteger.class, LISTEXPR_TO_EXPRS);
        addEntry(SymbolListExprs.class, TokenString.class, LISTEXPR_TO_EXPRS);
        addEntry(SymbolListExprs.class, TokenBoolean.class, LISTEXPR_TO_EXPRS);
    }

    private void addTerminalEntries() {
        addEntry(SymbolLparen.class, TokenLparen.class, LPAREN_TO_EPSILON);
        addEntry(SymbolRparen.class, TokenRparen.class, RPAREN_TO_EPSILON);
        addEntry(SymbolQuote.class, TokenQuote.class, QUOTE_TO_EPSILON);
        addEntry(SymbolInteger.class, TokenInteger.class, INTEGER_TO_EPSILON);
        addEntry(SymbolIdentifier.class, TokenIdentifier.class, IDENTIFIER_TO_EPSILON);
        addEntry(SymbolString.class, TokenString.class, STRING_TO_EPSILON);
        addEntry(SymbolEOF.class, TokenEOF.class, EOF_TO_EPSILON);
        addEntry(SymbolBoolean.class, TokenBoolean.class, BOOLEAN_TO_EPSILON);
    }

    public ReplacementRule findRule(Class<? extends Symbol> sType, Class<? extends Token> tType)
            throws SchemeInterpreterException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        if (!table.containsKey(sType)) {
            throw new SchemeInterpreterException("No predict table entry for " + sType);
        }
        else if (table.get(sType).containsKey(tType)) {
            return table.get(sType).get(tType);
        }
        else {
            throw makeMissingEntryException(sType, tType);
        }
    }

    private String expectedTokens(Class<? extends Symbol> sType)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        List<Class<? extends Token>> tokenTypes = table.get(sType).keySet().stream()
                .collect(toList());

        List<String> tokenReprs = new ArrayList<>();

        for (Class<? extends Token> tokenType : tokenTypes) {
            String repr = (String) tokenType.getMethod("repr").invoke(null);
            tokenReprs.add(format("`%s`", repr));
        }

        return tokenReprs.stream().collect(joining(", "));
    }

    private SchemeInterpreterException makeMissingEntryException(
            Class<? extends Symbol> sType,
            Class<? extends Token> tType)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        String expectedTokens = expectedTokens(sType);
        String actualToken = tType.getSimpleName();
        String message
                = format("Syntax error: encountered `%s`, "
                        + "expected one of %s.", actualToken, expectedTokens);

        return new SchemeInterpreterException(message);
    }

    private void addEntry(Class<? extends Symbol> sType, Class<? extends Token> tType,
            ReplacementRule rule) {
        if (!table.containsKey(sType)) {
            table.put(sType, new HashMap<>());
        }

        table.get(sType).put(tType, rule);
    }

}
