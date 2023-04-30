/**
 * Create a queue for the game baords
 * @param <E> type of queue that it is
 */
public class Queue<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public int added;

    public int removed;

    private boolean debug;

    /**
     * When DebugMode is activated, it will print
     * out the queue everytime its modified
     */
    public void DebugMode(){
        this.debug = true;
    }

    /**
     * Adds an element to the queue
     * @param element element to be added to the queue
     */
    public void Add(E element) {
        Node<E> newNode = new Node<>(element);
        // This will be the first element in the LinkedList
        if (head == null)
        {
            this.head = newNode;
            this.tail = newNode;
            this.size++;
            this.added++;
            if (this.debug)
            {
                this.printContents();
            }
            return;
        }
        this.tail.next = newNode;
        this.tail = newNode;
        this.size++;
        this.added++;
        if (this.debug)
        {
            this.printContents();
        }
    }

    /**
     * Removes the first element on the queue
     * @return the element that was removed
     */
    public E remove() {
        // Check if the head exists
        if (this.head != null) {
            // return the head element and assign the linked list head
            // to the next element
            E element = head.element;
            this.head = head.next;
            this.size--;
            this.removed++;
            //System.out.println("Deleted "+element+" from the LinkedList");
            return element;
        }
        // There is no head element
        return null;
    }

    /**
     *
     * @return returns the size of the queue
     */
    public int GetSize()
    {
        return this.size;
    }

    /**
     * Prints the contents of the queue
     */
    public void printContents() {
        Node<E> current;
        if (this.head != null)
        {
            current = head;
            System.out.print("["+current.element.toString());
            while(current.next != null)
            {
                System.out.print(", "+current.next.element.toString());
                current = current.next;
            }
            System.out.println("]");
        } else {
            System.out.println("[]");
        }
    }

    // Generate a class for the nodes
    private class Node<E> {
        E element;
        Node<E> next;

        public Node(E element)
        {
            this.element = element;
        }

        public Node(E element, Node<E> n)
        {
            this.element = element;
            this.next = n;
        }
    }
}



