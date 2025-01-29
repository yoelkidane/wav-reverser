import java.util.ConcurrentModificationException;
import java.util.EmptyStackException;
import java.util.Iterator;

/**
 * Implementation of BKStack, creates a stack using a linked list to store values as doubles.
 * This stack allows for a dynamically changing size as elements are pushed and popped.
 */
public class ListStack implements BKStack, Iterable<Double> {
    private ListStackNode stackPointer;
    private int modCount;

    /**
     * Constructor to initialize an empty stack
     */
    public ListStack() {
        stackPointer = null;
        modCount = 0;
    }


    /**
     * Checks if the stack is empty
     *
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        // Compare the pointer to its default value
        // If true then you know its default, otherwise it has content (false)
        return stackPointer == null;
    }

    /**
     * Uses a for-each loop to count the number of elements in the stack
     *
     * @return the number of elements in the stack
     */
    @Override
    public int count() {
        int counter = 0;

        // For each loop as-per instructions.
        // Could be optimized to instead be O(1) time.
        for (double junk : this) {
            counter++;
        }
        return counter;
    }


    /**
     * Pushes the given double value into the stack
     *
     * @param x The double which is being added to the stack
     */
    @Override
    public void push(double x) {
        ListStackNode newNode = new ListStackNode(x);
        newNode.next = stackPointer; // The new node points to the current top
        stackPointer = newNode; // New node becomes the top of the stack
        modCount++;
    }


    /**
     * Pops the most recent element out of the stack and returns its value
     *
     * @return the value of the double popped from the stack
     * @throws EmptyStackException if called on an empty stack
     */
    // Pops the top element from the stack
    @Override
    public double pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        double tempNode = stackPointer.data; // Hold the data of the to-be removed node
        stackPointer = stackPointer.next; // Change the pointer/ deletes the non-pointed to node
        modCount++;
        return tempNode;
    }


    /**
     * Sees what the most recently added element is without removing it
     *
     * @return the value of the double that is at the top of the stack
     * @throws EmptyStackException if called on an empty stack
     */
    // Sees what the most recent element is without removing it
    @Override
    public double peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stackPointer.data; // Only returns the most recent item in the stack

    }

    // Iterator Implementations


    /**
     * Returns an iterator over the elements
     *
     * @return an Iterator of type Doubles
     */
    @Override
    public Iterator<Double> iterator() {
        return new ListStackIterator();
    }

    private class ListStackIterator implements Iterator<Double> {
        private ListStackNode currentNode = stackPointer;
        private final int modCountDesired = modCount; // Stores desired modCount at time of initialization


        /**
         * Checks if there is an element which can be iterated to
         *
         * @return true if an element exists, false otherwise
         * @throws ConcurrentModificationException if the stack was improperly modified
         */
        @Override
        public boolean hasNext() {
            // Throw exception if the stack was altered
            if (modCount != modCountDesired) {
                throw new ConcurrentModificationException();
            }
            // Returns true if there is another element
            return currentNode != null;
        }


        /**
         * Holds and returns the next element in iteration
         *
         * @return the value of the next double in the stack
         * @throws ConcurrentModificationException if the stack was improperly modified
         */
        @Override
        public Double next() {
            // Throw exception if the stack was altered
            if (modCount != modCountDesired) {
                throw new ConcurrentModificationException();
            }
                // Stores data of node while pointer is still on it
                double tempNode = currentNode.data;
                currentNode = currentNode.next;
                return tempNode;
        }

    }

}
