package com.fz.circularbuffer;


/**
 * A random access circular buffer implementation with pre-set capacity.
 * <p/>
 * Inspired by {@link java.util.ArrayDeque}.
 */
public final class CircularBuffer<E> {

    private final Object[] elements;
    private int head;
    private int tail;

    public CircularBuffer(int numElements) {
        int initialCapacity = 8;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>> 1);
            initialCapacity |= (initialCapacity >>> 2);
            initialCapacity |= (initialCapacity >>> 4);
            initialCapacity |= (initialCapacity >>> 8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        elements = new Object[initialCapacity];
    }

    public void addFirst(E e) {
        elements[head = (head - 1) & (elements.length - 1)] = e;
        if (head == tail) {
            throw new IllegalStateException("Queue is full.");
        }
    }

    public void addLast(E e) {
        elements[tail] = e;
        tail = (tail + 1) & (elements.length - 1);
        if (tail == head) {
            throw new IllegalStateException("Queue is full.");
        }
    }

    @SuppressWarnings("unchecked")
    public E removeFirst() {
        int h = head;
        E result = (E) elements[h];
        // Element is null if deque empty
        if (result == null) {
            return null;
        }
        elements[h] = null;     // Must null out slot
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    @SuppressWarnings("unchecked")
    public E removeLast() {
        int t = (tail - 1) & (elements.length - 1);
        E result = (E) elements[t];
        if (result == null) {
            return null;
        }
        elements[t] = null;
        tail = t;
        return result;
    }

    @SuppressWarnings("unchecked")
    public E getFirst() {
        return (E) elements[head];
    }

    @SuppressWarnings("unchecked")
    public E getLast() {
        return (E) elements[(tail - 1) & (elements.length - 1)];
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        E result = (E) elements[(head + index) & (elements.length - 1)];
        return result;
    }

    public int size() {
        return (tail - head) & (elements.length - 1);
    }

    public boolean isEmpty() {
        return head == tail;
    }
}
