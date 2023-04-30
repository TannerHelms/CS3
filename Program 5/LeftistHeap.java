import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;

public class LeftistHeap<E extends Comparable<? super E>> {
    public Node<E> root;

    private int count;
    LeftistHeap(){
        root = null;
        count = 0;
    }
    public boolean isEmpty() {
        return root == null;
    }
    public void makeEmpty() {
        root = null;
    }



    public void Insert(E x) {
        count++;
        root = merge(root, new Node<>(x));
    }

    public void merge (LeftistHeap<E> heap) {
        if (this == heap) {return;};
        root = merge(root, heap.root);
        heap.root = null;
    }

    private Node<E> merge(Node<E> n1, Node<E> n2) {
        if (n1 == null) {return n2;};
        if (n2 == null) {return n1;};
        if (n1.element.compareTo(n2.element) > 0) {
            return merge1(n1, n2);
        } else {
            return merge1 (n2, n1);
        }

    }

    private Node<E> merge1(Node<E> n1, Node<E> n2) {
        if (n1.left == null){
            n1.left = n2;
        } else {
            n1.right = merge(n1.right, n2);
            if (n1.left.npl < n1.right.npl){
                var tmpNode = n1.left;
                n1.left = n1.right;
                n1.right = tmpNode;
            }
            n1.npl = n1.right.npl + 1;
        }
        return n1;
    }
    public E deleteMin() {
        if (isEmpty()){return null;};
        E ele = root.element;
        root = merge(root.left, root.right);
        count--;
        return ele;
    }

    public int Count(){
        return this.count;
    }
    private List<E> elements;

    public List<E> toArray() {
        elements = new LinkedList<>();
        toArray(root);
        return elements;
    }
    public void toArray(Node<E> node) {
        if (node == null || node.element == null){return;};
        elements.add(node.element);
        toArray(node.left);
        toArray(node.right);
    }

    public static class Node<E> {
        public E element;
        Node<E> left;
        Node<E> right;
        int npl;

        Node(E theElement) {
            this(theElement, null, null);
        }
        Node(E theElement, Node<E> lt, Node<E> rt) {
            element = theElement;
            left = lt;
            right = rt;
            npl = 0;
        }
    }
    public static void main(String[] args) {
        LeftistHeap<Integer> tree = new LeftistHeap<>();
        tree.Insert(4);
        tree.Insert(7);
        tree.Insert(2);
        tree.Insert(9);
        tree.Insert(12);
        tree.Insert(-1);
    }
}

