
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyQueue<Item> implements Iterable<Item> {
    private Node<Item> first;    
    private Node<Item> last;     
    private int N;               


    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    public MyQueue() {
        first = null;
        last = null;
        N = 0;
    }

    public void clear() {
        first = null;
        last = null;
         N= 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return N;
    }

    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return first.item;
    }

    public void enqueue(Item item) {

        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        N++;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        N--;
        if (isEmpty()) last = null;   
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }

    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public void addQueue(MyQueue<Item> queue) {
        if (!queue.isEmpty()) {
            Node<Item> oldFirst = first;
            if (isEmpty()) {
                first = queue.first;
                last = queue.last;
            } else {
                first = queue.first;
                queue.last.next = oldFirst;
            }

            N = N + queue.size();
        }

    }

}
