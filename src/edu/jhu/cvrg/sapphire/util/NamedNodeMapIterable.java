package edu.jhu.cvrg.sapphire.util;

/* 
 * Obtained from Stack Overflow at this address:
 * 
 * https://stackoverflow.com/questions/4171380/generic-foreach-iteration-of-namednodemap
 * 
 */

import java.util.Iterator;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class NamedNodeMapIterable implements Iterable<Node> {

    private final NamedNodeMap namedNodeMap;

    private NamedNodeMapIterable(NamedNodeMap namedNodeMap) {
        this.namedNodeMap = namedNodeMap;
    }

    public static NamedNodeMapIterable of(NamedNodeMap namedNodeMap) {
        return new NamedNodeMapIterable(namedNodeMap);
    }

    private class NamedNodeMapIterator implements Iterator<Node> {

        private int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return (namedNodeMap.getLength() > nextIndex);
        }
        @Override
        public Node next() {
            Node item = namedNodeMap.item(nextIndex);
            nextIndex = nextIndex + 1;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<Node> iterator() {
        return new NamedNodeMapIterator();
    }
}