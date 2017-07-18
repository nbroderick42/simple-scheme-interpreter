/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import static schemeinterpreter.evaluator.AtomLambda.evaluateListAndTakeLast;
import static schemeinterpreter.evaluator.Evaluator.assertTrue;
import static schemeinterpreter.evaluator.Atom.assertIsBoolean;
import static schemeinterpreter.evaluator.AtomList.toAtomList;

/**
 *
 * @author nick
 */
public enum BuiltinProcedure implements AtomProcedure {

    ADD("+") {

        @Override
        public Atom apply(AtomList args) {
            return applyArithmeticOperation(args, Math::addExact, 0);
        }

        @Override
        public String toString() {
            return "<builtin ADD>";
        }

    },
    SUBTRACT("-") {

        @Override
        public Atom apply(AtomList args) {
            return applyArithmeticOperation(args, Math::subtractExact, 0);
        }

        @Override
        public String toString() {
            return "<builtin SUBTRACT>";
        }

    },
    MULTIPLY("*") {

        @Override
        public Atom apply(AtomList args) {
            return applyArithmeticOperation(args, Math::multiplyExact, 1);
        }

        @Override
        public String toString() {
            return "<builtin MULTIPLY>";
        }

    },
    DIVIDE("/") {

        @Override
        public Atom apply(AtomList args) {
            try {
                return applyArithmeticOperation(args, Math::floorDiv, 1);
            }
            catch (ArithmeticException ae) {
                throw new EvaluationException("division by zero");
            }
        }

        @Override
        public java.lang.String toString() {
            return "<builtin DIVIDE>";
        }

    },
    CDR("cdr") {

        @Override
        public Atom apply(AtomList args) {
            assertTrue(args.isSingleton(), "cdr takes exactly one argument");

            Atom firstArg = args.getHead();
            assertTrue(firstArg.isList(), "argument to cdr must be a list");

            AtomList list = (AtomList) firstArg;
            assertTrue(!list.isEmpty(), "cdr cannot take tail of empty list");

            return list.getTail();
        }

        @Override
        public String toString() {
            return "<builtin CDR>";
        }

    },

    CAR("car") {

        @Override
        public Atom apply(AtomList args) {
            assertTrue(args.isSingleton(), "car takes exactly one argument");

            Atom firstArg = args.getHead();
            assertTrue(firstArg.isList(), "argument to car must be a list");

            AtomList list = (AtomList) firstArg.evaluate();

            return list.getHead();
        }

        @Override
        public String toString() {
            return "<builtin CAR>";
        }

    },
    CONS("cons") {

        @Override
        public Atom apply(AtomList args) {
            assertTrue(args.isPair(), "cons takes exactly two arguments");

            Atom firstArg = args.getHead();
            Atom secondArg = args.getTail().getHead();

            assertTrue(secondArg.isList(), "second argument to cons must be a list");
            AtomList list = (AtomList) secondArg;

            list.prepend(firstArg);

            return list;
        }

        @Override
        public String toString() {
            return "<builtin CONS>";
        }

    },
    LIST("list") {

        @Override
        public Atom apply(AtomList args) {
            return args.stream()
                    .map(Evaluator::evaluate)
                    .collect(toAtomList());
        }

        @Override
        public java.lang.String toString() {
            return "<builtin LIST>";
        }

    },
    DEFINE("define") {

        @Override
        public Atom apply(AtomList args) {
            assertTrue(args.size() == 2, "define takes exactly 2 arguments");

            Atom firstArg = args.getHead();
            assertTrue(firstArg.isIdentifier(), "first argument to define must be an identifier");

            Atom secondArg = args.getTail().getHead();

            AtomIdentifier id = (AtomIdentifier) firstArg;
            Atom expr = secondArg.evaluate();

            Evaluator.bindToCurrentFrame(id, expr);

            return AtomVoid.getInstance();
        }

        @Override
        public java.lang.String toString() {
            return "<builtin DEFINE>";
        }

    },
    LET("let") {

        @Override
        public Atom apply(AtomList args) {
            Function<Frame, Atom> impl = scope -> {

                assertTrue(args.size() >= 1, "let takes at least one argument");

                Atom firstArg = args.getHead();
                assertTrue(firstArg.isList(), "first argument to let must be a list");

                AtomList bindings = (AtomList) firstArg;

                Map<AtomIdentifier, Atom> valMap = new HashMap<>();

                bindings.forEach(atom -> {
                    assertTrue(atom.isListPair(), "first argument must be a list of pairs");
                    AtomList pair = (AtomList) atom;

                    Atom firstElem = pair.getHead();
                    assertTrue(firstElem.isIdentifier(), "pairs must be of the form (identifier, expr)");
                    AtomIdentifier id = (AtomIdentifier) firstElem;

                    if (valMap.containsKey(id)) {
                        throw new EvaluationException("cannot bind to already bound identifier `" + id + "'");
                    }
                    else {
                        Atom val = pair.getTail().getHead().evaluate();
                        valMap.put(id, val);
                    }
                });

                valMap.entrySet().stream().forEach(scope::bind);
                AtomList exprs = args.getTail();

                return evaluateListAndTakeLast(exprs);
            };

            return Evaluator.evaluateInLocalScope(impl);
        }

        @Override
        public java.lang.String toString() {
            return "<builtin LET>";
        }

    },
    LET_STAR("let*") {
        //TODO: assertions here
        @Override
        public Atom apply(AtomList args) {
            Function<Frame, Atom> impl = scope -> {

                assertTrue(args.size() >= 1, "let* takes at least one argument");

                Atom firstArg = args.getHead();
                assertTrue(firstArg.isList(), "first argument to let must be a list");

                AtomList bindings = (AtomList) args.getHead();

                bindings.forEach(atom -> {
                    assertTrue(atom.isListPair(), "first argument must be a list of pairs");
                    AtomList pair = (AtomList) atom;

                    Atom firstElem = pair.getHead();
                    assertTrue(firstElem.isIdentifier(), "pairs must be of the form (identifier, expr)");
                    AtomIdentifier id = (AtomIdentifier) firstElem;

                    Atom val = pair.getTail().getHead().evaluate();
                    scope.bind(id, val);
                });

                AtomList exprs = args.getTail();

                return evaluateListAndTakeLast(exprs);

            };

            return Evaluator.evaluateInLocalScope(impl);
        }

        @Override
        public String toString() {
            return "<builtin LET*>";
        }

    },
    SET_BANG("set!") {

        @Override
        public Atom apply(AtomList args) {
            assertTrue(args.size() == 2, "set! takes exactly two arguments");

            Atom firstArg = args.getHead();
            assertTrue(firstArg.isIdentifier(), "first argument to set! must be an identifier");
            AtomIdentifier id = (AtomIdentifier) firstArg;

            assertTrue(Evaluator.isBound(id), "identifier must be bound to a value");

            Atom prevValue = Evaluator.resolve(id);

            Atom secondArg = args.getTail().getHead();
            Atom nextValue = secondArg.evaluate();
            Evaluator.bindToCurrentFrame(id, nextValue);

            return prevValue;
        }

    },
    LAMBDA("lambda") {

        @Override
        public AtomImpl apply(AtomList args) {
            assertTrue(args.size() >= 1, "lambda takes at least one argument");

            Atom firstArg = args.getHead();
            assertTrue(firstArg.isList(), "first argument to lambda must be a list");

            AtomList params = (AtomList) firstArg;
            boolean allIdentifiers = params.allMatchType(AtomIdentifier.class);
            assertTrue(allIdentifiers, "first argument must be a list of identifiers");

            AtomList exprs = args.getTail();

            return AtomLambda.make(Evaluator.getCurrentFrame(), params, exprs);
        }

        @Override
        public String toString() {
            return "<builtin LAMBDA>";
        }

    },
    OR("or") {

        @Override
        public AtomImpl apply(AtomList args) {
            return args.stream()
                    .map(Evaluator::evaluate)
                    .peek(Atom::assertIsBoolean)
                    .map(AtomBoolean.class::cast)
                    .filter(AtomBoolean::isTrue)
                    .findFirst()
                    .orElse(AtomBoolean.getFalse());
        }

        @Override
        public java.lang.String toString() {
            return "<builtin OR>";
        }

    },
    AND("and") {

        @Override
        public AtomImpl apply(AtomList args) {

            return args.stream()
                    .map(Evaluator::evaluate)
                    .peek(Atom::assertIsBoolean)
                    .map(AtomBoolean.class::cast)
                    .filter(AtomBoolean::isFalse)
                    .findFirst()
                    .orElse(AtomBoolean.getTrue());
        }

        @Override
        public java.lang.String toString() {
            return "<builtin AND>";
        }

    },
    NOT("not") {

        @Override
        public AtomImpl apply(AtomList args) {
            assertTrue(args.size() == 1, "not takes exactly one argument");

            Atom firstArg = args.getHead();
            assertIsBoolean(firstArg);
            AtomBoolean val = (AtomBoolean) firstArg;

            return val.negate();
        }

        @Override
        public String toString() {
            return "<builtin NOT>";
        }

    },
    LESS_THAN("<") {

        @Override
        public AtomImpl apply(AtomList args) {
            return applyArithmeticComparisonOperation(args, BuiltinProcedure::lessThan);
        }

        @Override
        public String toString() {
            return "<builtin LESS_THAN>";
        }

    },
    GREATER_THAN(">") {

        @Override
        public AtomImpl apply(AtomList args) {
            return applyArithmeticComparisonOperation(args, BuiltinProcedure::greaterThan);
        }

        @Override
        public String toString() {
            return "<builtin GREATER_THAN>";
        }

    },
    EQUAL_INT("=") {

        @Override
        public AtomImpl apply(AtomList args) {
            return applyArithmeticComparisonOperation(args, BuiltinProcedure::equalToInt);
        }

        @Override
        public java.lang.String toString() {
            return "<builtin EQUAL_INT>";
        }

    },
    EQUAL_ATOM("equal?") {

        @Override
        public AtomImpl apply(AtomList args) {
            Atom first = args.getHead();
            Atom second = args.getTail().getHead();

            return AtomBoolean.make(first.equals(second));
        }

        @Override
        public String toString() {
            return "<builtin EQUAL_ATOM>";
        }

    },
    COND("cond") {

        @Override
        public Atom apply(AtomList args) {
            return args.stream()
                    .filter(Evaluator::isConditionTrue)
                    .findFirst()
                    .map(Evaluator::evaluateAndTakeSecond)
                    .orElse(AtomVoid.getInstance());
        }

        @Override
        public java.lang.String toString() {
            return "<builtin COND>";
        }

    },
    IF("if") {

        @Override
        public Atom apply(AtomList args) {
            assertTrue(args.size() == 3, "if takes exactly 3 arguments");

            Atom firstArg = args.getHead().evaluate();

            Atom trueExpr = args.getTail().getHead();
            Atom falseExpr = args.getTail().getTail().getHead();

            if (firstArg.isBoolean()) {
                AtomBoolean test = (AtomBoolean) firstArg;
                return test.isTrue() ? trueExpr.evaluate() : falseExpr.evaluate();
            }
            else {
                return falseExpr.evaluate();
            }
        }
    },
    RAISE("raise") {

        @Override
        public Atom apply(AtomList args) {
            assertTrue(args.size() == 1, "raise takes exactly one argument");
            Atom firstArg = args.getHead();

            throw new SchemeException(firstArg.evaluate());
        }

    },
    GUARD("guard") {

        @Override
        public Atom apply(AtomList args) {
            assertTrue(args.size() >= 3, "guard takes at least 3 arguments");

            Atom firstArg = args.getHead();
            assertTrue(firstArg.isIdentifier(), "first argument to guard must be an identifier");
            AtomIdentifier exv = (AtomIdentifier) firstArg;

            Atom secondArg = args.getTail().getHead();
            assertTrue(secondArg.isList(), "second argument to guard must be a list");

            AtomList handlers = (AtomList) secondArg;
            boolean allListPairs = handlers.stream().allMatch(pair -> pair.isListPair());
            assertTrue(allListPairs, "second argument to guard must be a list of pairs");

            AtomList exprs = args.getTail().getTail();

            try {
                return evaluateListAndTakeLast(exprs);
            }
            catch (SchemeException se) {
                Function<Frame, Atom> handleExc = scope -> {
                    scope.bind(exv, se.getValue());
                    
                    return handlers.stream()
                            .filter(Evaluator::isConditionTrue)
                            .findFirst()
                            .map(Evaluator::evaluateAndTakeSecond)
                            .orElseThrow(() -> se);
                };

                return Evaluator.evaluateInLocalScope(handleExc);
            }
        }

    };

    private boolean lazy;

    final String token;

    private BuiltinProcedure(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean isLazy() {
        return lazy;
    }

    @Override
    public Atom evaluate() {
        return Evaluator.evaluate(this);
    }

    private static AtomImpl applyArithmeticOperation(
            AtomList args,
            BinaryOperator<Integer> op,
            Integer identity) 
    {
        
        Integer result = args.stream()
                .map(Atom::evaluate)
                .peek(Atom::assertIsInteger)
                .map(AtomInteger.class::cast)
                .map(AtomInteger::getValue)
                .reduce(op)
                .orElse(identity);

        return AtomInteger.make(result);
    }

    private static AtomBoolean applyArithmeticComparisonOperation(
            AtomList args,
            BiPredicate<AtomInteger, AtomInteger> op) 
    {
        assertTrue(args.size() == 2, "integer comparison operation takes exactly two arguments");

        Atom firstArg = args.getHead();
        Atom secondArg = args.getTail().getHead();

        Atom firstEval = firstArg.evaluate();
        Atom secondEval = secondArg.evaluate();

        assertTrue(firstEval.isInteger(), "integer comparison requires two integers");
        assertTrue(secondEval.isInteger(), "integer comparison requires two integers");

        AtomInteger firstInt = (AtomInteger) firstEval;
        AtomInteger secondInt = (AtomInteger) secondEval;

        return op.test(firstInt, secondInt) ? AtomBoolean.getTrue() : AtomBoolean.getFalse();
    }

    private static boolean lessThan(AtomInteger first, AtomInteger second) {
        return first.getValue().compareTo(second.getValue()) < 0;
    }

    private static boolean greaterThan(AtomInteger first, AtomInteger second) {
        return first.getValue().compareTo(second.getValue()) > 0;
    }

    private static boolean equalToInt(AtomInteger first, AtomInteger second) {
        return Objects.equals(first.getValue(), second.getValue());
    }

}
