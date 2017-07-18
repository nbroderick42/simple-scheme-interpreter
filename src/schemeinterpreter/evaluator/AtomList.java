/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemeinterpreter.evaluator;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collector;
import static java.util.stream.Collector.of;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import static java.util.stream.Stream.builder;
import schemeinterpreter.evaluator.Evaluator;

/**
 *
 * @author nick
 */
public class AtomList extends AtomImpl {

    private Integer size;

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

    private AtomList() {
    }

    private AtomList(Node first, Node last) {
        this.first = first;
        this.last = last;
    }

    public static AtomList makeEmptyList() {
        return new AtomList();
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

    public AtomList merge(AtomList other) {
        Node newFirst = first != null ? first : other.getFirst();
        Node newLast = other.getLast() != null ? other.getLast() : last;
        AtomList result = new AtomList(newFirst, newLast);
        if (last != null) {
            last.setNext(other.getFirst());
        }
        return result;
    }

    public AtomList getTail() {
        return new AtomList(first.getNext(), last);
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
    public Atom evaluate() {
        return Evaluator.getInstance().evaluate(this);
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
        Stream.Builder<Atom> builder = builder();
        forEach(builder::add);
        return builder.build();
    }

    public static Collector<Atom, ?, AtomList> toAtomList() {
        return of(AtomList::new, AtomList::append,
                AtomList::merge, new Collector.Characteristics[]{});
    }

    @Override
    public String toString() {
        return stream().map(Atom::toString).collect(joining(" ", "( ", " )"));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AtomList) {
            AtomList other = (AtomList) o;
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
        return stream().mapToInt((schemeinterpreter.evaluator.Atom node) -> Objects.hashCode(node)).reduce(3, (result, hash) -> 83 * result + hash);
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
