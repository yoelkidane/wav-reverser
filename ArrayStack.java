import java.util.EmptyStackException;

/**
 * Implementation of BKStack, creates a stack using an array to store values as doubles.
 * The stack doubles in size whenever the array is full.
 */
public class ArrayStack implements BKStack {
    private static final int INITIAL_CAPACITY = 5;
    private double[] stack;
    private int stackPointer;

    /**
     *  Constructor creates stack (an array) with initial capacity (5)
     */
    public ArrayStack() {
        stack = new double[INITIAL_CAPACITY];
        stackPointer = -1; // Setting point for an empty stack
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
        return stackPointer == -1;
    }


    /**
     * Returns the number of elements currently in the stack
     *
     * @return the index of the pointer + 1 = the num of elements in the stack
     */
    @Override
    public int count() {
        return stackPointer + 1;
    }


    /**
     * Pushes the given double value into the stack
     * When the stack is full, it calls for the array to be doubled with resize
     *
     * @param x the double which is being added to the stack
     */
    @Override
    public void push(double x) {
        // Resize stack only when required for optimization
        if (stackPointer == stack.length - 1) {
            resize();
        }
        // Move the stack pointer, then add the new value
        stack[++stackPointer] = x;
    }


    /**
     * Pops the most recent element out of the stack and returns its value
     *
     * @return the value of the double popped from the stack
     * @throws EmptyStackException if called on an empty stack
     */
    @Override
    public double pop() {
        if (isEmpty()) {
            throw new EmptyStackException(); // Throws exception if the stack is empty
        }
        // Returns the most recent item in the stack then shifts the pointer to send it to garbage collection
        return stack[stackPointer--];
    }


    /**
     * Sees what the most recently added element is without removing it
     *
     * @return the value of the double that is at the top of the stack
     * @throws EmptyStackException if called on an empty stack
     */
    @Override
    public double peek() {
        if (isEmpty()) {
            throw new EmptyStackException(); // Throws exception if the stack is empty
        }
        // Only returns the most recent item in the stack
        return stack[stackPointer];
    }


    /**
     * Doubles the size of the stack
     * Only called if attempting to add a value to an already full stack
     * This is O(n) time as it uses arraycopy
     */
    private void resize() {
        double[] tempStack = new double[stack.length * 2]; // Temp array of double length

        // O(n)-time array copy. only called when required
        System.arraycopy(stack, 0, tempStack, 0, stack.length);

        stack = tempStack; // Changes stack to point towards new array, old array is garbage collected
    }
}
