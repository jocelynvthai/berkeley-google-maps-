package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<Node> heap;
    private HashSet<T> items;
    private HashMap<T, Integer> indexes;
    private int size;

    private class Node {
        T val;
        double priority;
        public Node(T val, double priority) {
            this.val = val;
            this.priority = priority;
        }
    }

    public ArrayHeapMinPQ() {
        heap = new ArrayList<>();
        items = new HashSet<>();
        indexes = new HashMap<>();
        size = 0;

        //start index at 1
        heap.add(new Node(null, 0));
    }

    /** Adds an item of type T with the given priority value.
     * Throws an IllegalArgumentException if item is already present.
     * You may assume that item is never null.
     */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("item already present in heap");
        }
        heap.add(new Node(item, priority));
        items.add(item);
        size++;
        indexes.put(item, size);
        swim(item);
    }

    /** Returns true if the PQ contains the given item.
     */
    @Override
    public boolean contains(T item) {
        return items.contains(item);
    }

    /** Returns the item with smallest priority.
     * If no items exist, throw a NoSuchElementException.
     */
    @Override
    public T getSmallest() {
        if (size == 0) {
            throw new NoSuchElementException("heap empty, can't get smallest");
        }
        return heap.get(1).val;
    }

    /** Removes and returns the item with smallest priority.
     * If no items exist, throw a NoSuchElementException.
     */
    @Override
    public T removeSmallest() {
        if (size == 0) {
            throw new NoSuchElementException("heap empty, can't remove");
        }
        swap(1, size);
        Node removed = heap.remove(size);
        items.remove(removed.val);
        indexes.remove(removed.val);
        size--;
        if (size != 0) {
            sink(heap.get(1).val);
        }
        return removed.val;
    }

    /** Returns the number of items.
     */
    @Override
    public int size() {
        return size;
    }

    /** Sets the priority of the given item to the given value.
     * If the item does not exist, throw a NoSuchElementException.
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("cannot change priority");
        }

        int itemIndex = indexes.get(item);
        double lastPriority = heap.get(itemIndex).priority;
        heap.get(itemIndex).priority = priority;
        if (priority < lastPriority) {
            swim(item);
        }
        if (priority > lastPriority) {
            sink(item);
        }
    }

    /** continue swimming item up until it is larger than its parent
     */
    private void swim(T item) {
        int itemIndex = indexes.get(item);
        while (itemIndex > 1 && larger(itemIndex / 2, itemIndex)) {
            swap(itemIndex / 2, itemIndex);
            itemIndex = itemIndex / 2;
        }
    }

    /** continue sinking item until it is smaller than it's children
     * go through two children (index * 2 and index * 2 + 1)
     */
    private void sink(T item) {
        int itemIndex = indexes.get(item);
        while (itemIndex * 2 <= size) {
            int childIndex = itemIndex * 2;

            // choose smaller child
            if (childIndex < size && larger(childIndex, childIndex + 1)) {
                childIndex++;
            }

            if (larger(itemIndex, childIndex)) {
                swap(itemIndex, childIndex);
                itemIndex = childIndex;
            } else {
                itemIndex = size;
            }
        }
    }

    /** returns whether node at index a is larger than node at index b
     */
    private boolean larger(int a, int b) {
        Node aNode = heap.get(a);
        Node bNode = heap.get(b);
        return aNode.priority > bNode.priority;
    }

    /** swap the two nodes at the indexes, a and b
     * @source https://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html
     */
    private void swap(int a, int b) {
        Node aNode = heap.set(a, heap.get(b));
        Node bNode = heap.set(b, aNode);
        indexes.put(bNode.val, a);
        indexes.put(aNode.val, b);
    }
}
