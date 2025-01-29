/**
 * Defines what a node is for the linked list in ListStack
 * Each node contains the data as a double and a reference pointing to the next node
 */
public class ListStackNode {
    double data; // Value stored in the node
    ListStackNode next; // Pointer to the next node

    /**
     * Initialize node with given value and a next null pointer
     *
     * @param data the value to be stored as a double in the node
     */
    public ListStackNode(double data) {
        this.data = data;
        this.next = null;
    }
}
