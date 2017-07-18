/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import schemeinterpreter.parser.Symbol;

/**
 *
 * @author nick
 */

public abstract class AtomImpl implements Atom {
    
    private boolean lazy;

    @Override
    public boolean isIdentifier() {
        return this instanceof Identifier;
    }

    @Override
    public boolean isString() {
        return this instanceof String;
    }

    @Override
    public boolean isInteger() {
        return this instanceof Integer;
    }

    @Override
    public boolean isVoid() {
        return this instanceof Void;
    }

    @Override
    public boolean isBoolean() {
        return this instanceof Boolean;
    }

    @Override
    public boolean isList() {
        return this instanceof List;
    }

    @Override
    public boolean isOperation() {
        return this instanceof Operation;
    }
    
    public static class List extends AtomImpl {

        private java.lang.Integer size;
        
        private class Node {
            private final Atom atom;
            private Node next;
            
            public Node(Atom atom) {
                this.atom = atom;
            }
            
            private void setNext(Node next) {
                this.next = next;
            }
            
            private Node getNext() {
                return next;
            }
            
            private Atom getAtom() {
                return atom;
            }
        }
        
        private Node first;
        private Node last;
        
        private List() {
        }
        
        private List(Node first, Node last) {
            this.first = first;
            this.last = last;
        }
        
        public static List makeEmptyList() {
            return new List();
        }
        
        public void prepend(Atom atom) {
            Node node = new Node(atom);
            
            node.setNext(first);
            first = node;
            
            if (last == null) {
                last = node;
            }
            
            incSize();
        }
        
        public void append(Atom atom) {
            Node node = new Node(atom);
            
            if (first == null) {
                first = node;
            }
            else {
                last.setNext(node);
            }
            last = node;
            
            incSize();
        }
        
        public List merge(List other) {
            Node newFirst = first != null ? first : other.getFirst();
            Node newLast = other.getLast() != null ? other.getLast() : last;
            
            List result = new List(newFirst, newLast);
            
            if (last != null) {
                last.setNext(other.getFirst());
            }
            
            return result;
        }
        
        public List getTail() {
            return new List(first.getNext(), last);
        }
        
        public Atom getHead() {
            return first.getAtom();
        }
        
        private Node getFirst() {
            return first;
        }
        
        private Node getLast() {
            return last;
        }
        
        @Override
        public Atom evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
        
        public boolean isEmpty() {
            return first == null;
        }
        
        public void forEach(Consumer<Atom> consumer) {
            for (Node curr = first; curr != null; curr = curr.getNext()) {
                consumer.accept(curr.getAtom());
            }
        }
        
        public Stream<Atom> stream() {
            Stream.Builder<Atom> builder = Stream.builder();
            forEach(builder::add);
            
            return builder.build();
        }
        
        public static Collector<Atom, ?, List> toAtomList() {
            return Collector.of(List::new, List::append, List::merge, new Collector.Characteristics[] {});
        }
        
        @Override 
        public java.lang.String toString() {
            return stream()
                    .map(Atom::toString)
                    .collect(joining(" ", "( ", " )"));
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof List) {
                List other = (List)o;
                
                if (isEmpty() && other.isEmpty()) {
                    return true;
                }
                else if (other.getHead().equals(getHead())) {
                    return other.getTail().equals(getTail());
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return stream()
                .mapToInt(node -> Objects.hashCode(node))
                .reduce(3, (result, hash) -> 83 * result + hash);
        }
        
        public int size() {
            if (size == null) {
                int count = 0;
                for (Node curr = first; curr != null; curr = curr.getNext()) {
                    count++;
                }
                size = count;
            }
            
            return size;
        }
        
        private void incSize() {
            if (size == null) {
                size();
                size++;
            }
        }
        
        public boolean isPair() {
            return size() == 2;
        }
        
        public boolean isSingleton() {
            return size() == 1;
        }
        
        public boolean allMatchType(Class<? extends Atom> type) {
            return stream().allMatch(type::isInstance);
        }
    }
    
    public static class Identifier extends AtomImpl {
        
        private final java.lang.String val;
        
        private Identifier(java.lang.String val) {
            this.val = val;
        }
        
        public static Identifier make(Symbol.Identifier val) {
            return new Identifier(val.getValue());
        }
        
        public static Identifier make(java.lang.String val) {
            return new Identifier(val);
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof AtomImpl.Identifier) {
                return ((AtomImpl.Identifier)o).getValue().equals(val);
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 43 * hash + Objects.hashCode(this.val);
            return hash;
        }
        
        public java.lang.String getValue() {
            return val;
        }

        @Override
        public Atom evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
        
        @Override 
        public java.lang.String toString() {
            return val;
        }
       
    }

    public static class Integer extends AtomImpl {
        
        private final java.lang.Integer val;
        
        public static Integer make(Symbol.Integer integer) {
            return new Integer(integer.getValue());
        }
        
        public static Integer make(java.lang.Integer integer) {
            return new Integer(integer);
        }

        private Integer(java.lang.Integer val) {
            this.val = val;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof AtomImpl.Integer) {
                return ((AtomImpl.Integer)o).getValue().equals(val);
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 41 * hash + Objects.hashCode(this.val);
            return hash;
        }

        public java.lang.Integer getValue() {
            return val;
        }

        @Override
        public Atom evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
        
        @Override 
        public java.lang.String toString() {
            return val.toString();
        }
    }

    public static class String extends AtomImpl {

        private final java.lang.String val;
        
        public String(java.lang.String val) {
            this.val = val;
        }
        
        public static AtomImpl.String make(Symbol.String string) {
            return new AtomImpl.String(string.getValue());
        }
        
        public java.lang.String getValue() {
            return val;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof AtomImpl.String) {
                return ((AtomImpl.String)o).getValue().equals(val);
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + Objects.hashCode(this.val);
            return hash;
        }

        @Override
        public Atom evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
        
        @Override 
        public java.lang.String toString() {
            return val;
        }
    }
    
    public interface Operation extends Atom {
        public Atom apply(Evaluator evaluator, AtomImpl.List args); 
    }
    
    public static class Lambda extends AtomImpl implements Operation {
        
        private Frame closingFrame;
        private List params;
        private List exprs;
        
        private Lambda() {}
        
        private Lambda(Frame closingFrame, List params, List exprs) {
            this.closingFrame = closingFrame;
            this.params = params;
            this.exprs = exprs;
        }
        
        public static Lambda make(Evaluator evaluator, List params, List exprs) {
            return new Lambda(evaluator.getCurrentFrame(), params, exprs);
        }
        
        @Override
        public Atom evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
        
        @Override
        public Atom apply(Evaluator evaluator, List args) {
            Function<Frame, Atom> impl = scope -> {
                List currParams = params;
                List currArgs = args;

                while (!currParams.isEmpty() && !currArgs.isEmpty()) {
                    AtomImpl.Identifier id = (AtomImpl.Identifier) currParams.getHead();
                    Atom expr = args.getHead().evaluate(evaluator);

                    scope.bind(id, expr);

                    currParams = currParams.getTail();
                    currArgs = currArgs.getTail();
                }
                return evaluateListAndTakeLast(evaluator, exprs);
            };
            
            return evaluator.evaluateInScope(closingFrame, impl);
        }
        
        public static Atom evaluateListAndTakeLast(Evaluator evaluator, AtomImpl.List exprs) {
            return exprs.stream()
                        .map(atom -> atom.evaluate(evaluator))
                        .reduce(AtomImpl.Void.getInstance(), Lambda::takeLast);
        }
            
        @Override 
        public java.lang.String toString() {
            return "<lambda>";
        }
        
        private static Atom takeLast(Atom first, Atom second) {
            return second;
        }
    }
    
    public static class Boolean extends AtomImpl {
        
        private static final Boolean FALSE = new Boolean();
        private static final Boolean TRUE = new Boolean();

        public static Boolean make(Symbol.Boolean bool) {
            return bool.getValue() ? getTrue() : getFalse();
        }
        
        public static Boolean make(boolean bool) {
            return bool ? getTrue() : getFalse();
        }
        
        public Boolean negate() {
            return isFalse() ? getTrue() : getFalse();
        }

        @Override
        public AtomImpl evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
        
        public boolean isTrue() {
            return this == getTrue();
        }
        
        public boolean isFalse() {
            return this == getFalse();
        }
        
        public static Boolean getTrue() {
            return TRUE;
        }
        
        public static Boolean getFalse() {
            return FALSE;
        }

        @Override
        public java.lang.String toString() {
            return isTrue() ? "#t" : "#f";
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof AtomImpl.Boolean) {
                AtomImpl.Boolean bool = (AtomImpl.Boolean) o;
                return (isTrue() && bool.isTrue()) || (isFalse() && bool.isFalse());
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }
    }
    
    public static class Void extends AtomImpl {
        
        private static final Void INSTANCE = new Void();
        
        @Override
        public AtomImpl evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
        
        public static Void getInstance() {
            return INSTANCE;
        }
        
        @Override 
        public java.lang.String toString() {
            return "<void>";
        }
        
    }
    
    public abstract Atom evaluate(Evaluator evaluator);
    
    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

}
