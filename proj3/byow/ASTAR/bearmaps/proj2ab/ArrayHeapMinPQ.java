package byow.ASTAR.bearmaps.proj2ab;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private class Node {
        private T item;
        private double priority;

        Node(T thing, double p) {
            item = thing;
            priority = p;
        }
    }

    private int size;
    private ArrayList<Node> heep;
    private HashMap<T, Integer> items;

    /* Constructs an instance of ArrayHeapMinPQ. */
    public ArrayHeapMinPQ() {
        size = 0;
        heep = new ArrayList<>();
        heep.add(new Node(null, 3.69)); //ensure index 0 is filled
        items = new HashMap<>();
    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present. */
    public void add(T item, double priority) throws IllegalArgumentException {
        if (contains(item)) {
            throw new IllegalArgumentException("Small Sad");
        }
        Node thing = new Node(item, priority);
        heep.add(thing);
        size++;
        items.put(item, size);
        swim(size);

    }

    /* Swaps the two nodes given at the particular two indexes. */
    private void swaperoo(int first, int second) {
        items.put(heep.get(first).item, second);
        items.put(heep.get(second).item, first);
        Node temp = heep.get(second);
        heep.set(second, heep.get(first));
        heep.set(first, temp);
    }

    /* Returns the parent index of a particular index. */
    private int parent(int index) {
        return index / 2;
    }

    /* Returns the index of the left kid, 0 if kid doesn't exist. */
    private int leftKid(int index) {
        if (index * 2 > size) {
            return 0;
        }
        return index * 2;
    }

    /* Returns the index of the right kid, 0 if kid doesn't exist. */
    private int rightKid(int index) {
        if (index * 2 + 1 > size) {
            return 0;
        }
        return index * 2 + 1;
    }


    /* Bumps up a node at a given index if it's priority is higher than its parent. */
    private void swim(int index) {
        if (index == 1) {
            return;
        }
        int parent = parent(index);
        if (compare(index, parent) < 0) {
            swaperoo(index, parent);
            swim(parent);
        }
    }

    /* Returns true if the PQ contains the given item. */
    public boolean contains(T item) {
        return items.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T getSmallest() throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException("Big Sad");
        }
        return heep.get(1).item;
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T removeSmallest() throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException("Huge Sad");
        }
        Node lowP = heep.get(1);
        swaperoo(1, size);
        items.remove(heep.get(size).item);
        heep.remove(size);
        size--;
        sink(1);
        return lowP.item;
    }

    /* Returns negative number if first priority is lower than second, positive if vice versa.
    Returns 0 if two priorities are equivalent. */
    private int compare(int first, int second) {
        return Double.compare(heep.get(first).priority, heep.get(second).priority);
    }


    /* Drops an element down the PQ to it's rightful place. */
    private void sink(int index) {
        /*while (leftKid(index) != 0) {
            int favKid = leftKid(index);
            if (rightKid(index) != 0 && compare(favKid, rightKid(index)) >= 0) {
                favKid = rightKid(index);
            }
            if (compare(index, favKid) < 0) {
                break;
            }
            items.put(heep.get(index).item, favKid);
            items.put(heep.get(favKid).item, index);
            swaperoo(index, favKid);
            index = favKid;
        }*/
        if (leftKid(index) == 0) {
            return;
        }
        int favKid = leftKid(index);
        if (rightKid(index) != 0 && compare(favKid, rightKid(index)) >= 0) {
            favKid = rightKid(index);
        }
        if (compare(index, favKid) < 0) {
            return;
        }
        swaperoo(index, favKid);
        sink(favKid);
    }

    /* Returns the number of items in the PQ. */
    public int size() {
        return size;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    public void changePriority(T item, double priority) throws NoSuchElementException {
        if (!contains(item)) {
            throw new NoSuchElementException("Massive Sad");
        }
        double oldp = heep.get(items.get(item)).priority;
        heep.get(items.get(item)).priority = priority;
        if (Double.compare(oldp, priority) > 0) {
            swim(items.get(item));
        } else {
            sink(items.get(item));
        }
    }
}
