// ******************ERRORS********************************
// Throws UnderflowException as appropriate


import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Vector;

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     *
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree<E extends Comparable<? super E>> {
    private BinaryNode<E> root;  // Root of tree
    private String treeName;     // Name of tree

    /**
     * Create an empty tree
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create tree from list
     * @param arr   List of elements
     * @param label Name of tree
     * @ordered true if want an ordered tree
     */
    public Tree(E[] arr, String label, boolean ordered) {
        treeName = label;
        if (ordered) {
            root = null;
            for (int i = 0; i < arr.length; i++) {
                bstInsert(arr[i]);
            }
        } else root = buildUnordered(arr, 0, arr.length - 1);
    }

    /**
     * Build a NON BST tree by inorder
     * * This routine runs in O(n)
     * @param arr nodes to be added
     * @return new tree
     */
    private BinaryNode<E> buildUnordered(E[] arr, int low, int high) {
        if (low > high) return null;
        int mid = (low + high) / 2;
        BinaryNode<E> curr = new BinaryNode<>(arr[mid], null, null);
        curr.left = buildUnordered(arr, low, mid - 1);
        curr.right = buildUnordered(arr, mid + 1, high);
        return curr;
    }


    /**
     * Change name of tree
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * Return a string displaying the tree contents of the BST
     */
    public String toString() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + "\n" + printBST(root, 0);
    }

    private String printBST(BinaryNode<E> node, int height)
    {
        if (node == null || node.element == null){
            return "";
        }
        String indent = " ".repeat(Math.max(0, height * 2));
        return printBST(node.right, height + 1) +
                indent + node.element.toString() +
                "\n" + printBST(node.left, height + 1);
    }

    public String inOrder()
    {
        return inOrder(root);
    }
    private String inOrder(BinaryNode<E> node)
    {
        if (node == null || node.element == null)
        {
            return "";
        }
        return inOrder(node.left) + node.element.toString() + " " + inOrder(node.right);
    }
    /**
     * Return a string displaying the tree contents as a single line
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root);
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(n)
     *
     * @param t the node that roots the subtree.
     */
    private String toString2(BinaryNode<E> t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.left));
        sb.append(t.element.toString() + " ");
        sb.append(toString2(t.right));
        return sb.toString();
    }


    /**
     * The complexity of finding the deepest node is O(n)
     * @return
     */
    private int maxHeight = 0;
    private BinaryNode<E> maxNode;
    public E deepestNode() {
        deepestNode(root, 0);
        return maxNode.element;
    }

    private void deepestNode(BinaryNode<E> node, int height){
        if (node == null || node.element == null){
            return;
        }
        if (height > maxHeight)
        {
            maxHeight = height;
            maxNode = node;
        }
        deepestNode(node.left, height + 1);
        deepestNode(node.right, height + 1);

    }



    /**
     * The complexity of finding the flip is O(n)
     * reverse left and right children recursively
     */
    public void flip() {
        flip(root);
    }
    public void flip(BinaryNode<E> node)
    {
        if (node == null){return;};

        BinaryNode<E> tempLeft = node.left;
        node.left = node.right;
        node.right = tempLeft;
        flip(node.left);
        flip(node.right);
    }

    /**
     * Counts number of nodes in specified level
     * The complexity of nodesInLevel is O(n)
     * @param level Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int level) {
        return nodesInLevel(root, level, 0);
    }

    private int nodesInLevel(BinaryNode<E> node, int level, int currHeight){
        if (node == null || level < currHeight) return 0;
        if (level == currHeight){return 1;};
        return nodesInLevel(node.left, level, currHeight + 1) + nodesInLevel(node.right, level, currHeight + 1);
    }

    /**
     * Print all paths from root to leaves
     * The complexity of printAllPaths is O(n)
     */
    public void printAllPaths() {
        printAllPaths(root, "");
    }
    private void printAllPaths(BinaryNode<E> node, String currPath){
        if (node == null){
            return;
        };

        currPath = currPath + node.element.toString() + " ";
        if (node.left == null && node.right == null){
            System.out.println(currPath);
        } else {
            printAllPaths(node.left, currPath);
            printAllPaths(node.right, currPath);
        }
    }


    /**
     * Counts all non-null binary search trees embedded in tree
     *  The complexity of countBST is O(n)
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        return root != null ? countBST(root) : 0;
    }

    private Integer countBST(BinaryNode<E> node) {
        if (node == null){return 0;};
        int leftCount = countBST(node.left);                     // Count the number of BSTs on the left side
        int rightCount = countBST(node.right);                   // Count the number of BSTs on the right side
        return leftCount + rightCount + isBST(node);             // Return the # of BSTS on the left + right and if the root is a BST
    }

    private int isBST(BinaryNode<E> node) {
        if (node == null){return 1;}               // If the node is null, its not a BST
        if (node.left == null && node.right == null) return 1; // If the node is a leaf, then its a BST
        if (isBST(node.left) == 1 &&               // Check if the left side is a BST
                isBST(node.right) == 1 &&          // Check if the right side is a BST
                isMax(node.left, node.element) &&  // Make sure everything to the left is smaller
                isMin(node.right, node.element)){  // Make sure everything to the right is smaller
            return 1;
        }
        return 0;
    }

    private boolean isMin(BinaryNode<E> node, E value){
        if (node == null){return true;};
        if (value.compareTo(node.element) > 0) return false;
        return isMin(node.left, value) && isMin(node.right, value);
    }

    private boolean isMax(BinaryNode<E> node, E value){
        if (node == null) return true;
        if (value.compareTo(node.element) < 0) return false;
        return isMax(node.left, value) && isMax(node.right, value);
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     * The complexity of bstInsert depends on the tree.  If it is balanced the complexity is O(log n)
     * @param x the item to insert.
     */
    public void bstInsert(E x) {

        root = bstInsert(x, root);
    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<E> bstInsert(E x, BinaryNode<E> t) {
        if (t == null)
            return new BinaryNode<E>(x, null, null);
        int compareResult = x.compareTo(t.element);
        if (compareResult < 0) {
            t.left = bstInsert(x, t.left);
        } else {
            t.right = bstInsert(x, t.right);
        }
        return t;
    }

    /**
     * Determines if item is in tree
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(E item) {
        return contains(item, root);
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains(E x, BinaryNode<E> t) {
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else {
            return true;    // Match
        }
    }
    /**
     * Remove all paths from tree that sum to less than given value
     * This routine runs in O(n)
     * @param sum: minimum path sum allowed in final tree
     */
    public void pruneK(Integer sum) {
        root = (BinaryNode<E>) pruneK(sum, 0, (BinaryNode<Integer>) root);
    }

    private BinaryNode<Integer> pruneK(Integer sum, int total, BinaryNode<Integer> node){
        if (node == null){return null;};
        total += node.element;

        node.left=pruneK(sum, total, node.left);
        node.right=pruneK(sum, total, node.right);

        if (node.left == null && node.right == null & total < sum){
            node = null;
        }
        return node;
    }

    /**
     * Build tree given inOrder and preOrder traversals.  Each value is unique
     * This routine runs in O(n)
     * @param inOrder  List of tree nodes in inorder
     * @param preOrder List of tree nodes in preorder
     */
    public void buildTreeTraversals(E[] inOrder, E[] preOrder) {
        if (inOrder != null && preOrder != null){
            root = buildTreeTraversals(inOrder, preOrder, 0, inOrder.length - 1, 0, inOrder.length - 1);
        }
    }

    private BinaryNode<E> buildTreeTraversals(E[] inOrder, E[] preOrder, int inStart, int inEnd, int preStart, int preEnd){
        if (preStart > preEnd || inStart > inEnd) return null;
        if (inStart == inEnd) return new BinaryNode<>(inOrder[inStart]);
        BinaryNode<E> currNode = new BinaryNode<>(preOrder[preStart]);
        int LeftSide = indexOf(inOrder, preOrder[preStart]);
        currNode.left = buildTreeTraversals(inOrder, preOrder, inStart, LeftSide - 1, preStart + 1, indexOf(preOrder, inOrder[inStart]));
        int rightSide = indexOf(inOrder, currNode.element) + 1;
        currNode.right = buildTreeTraversals(inOrder, preOrder, rightSide, inEnd, rightSide, inOrder.length - 1);
        return currNode;
    }
    // Find the index in the array given a element
    private int indexOf(E[] arr, E element){
        if (arr == null) return -1;
        for (int j = 0; j < arr.length; j++) {
            if (arr[j].equals(element)) return j;
        }
        return -1;
    }

    /**
     * Find the least common ancestor of two nodes
     * This routine runs in O(H) where H is the height of the tree
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public String lca(E a, E b) {
        if (!contains(a) || !contains(b)) return "None";
        BinaryNode<E> ancestor = lca(root, a, b);
        if (ancestor == null) return "none";
        else return ancestor.toString();
    }

    private BinaryNode<E> lca(BinaryNode<E> node, E a, E b){
        if (node == null) return null;
        if (node.element.compareTo(a) > 0 && node.element.compareTo(b) > 0){
            return lca(node.left, a, b);
        }
        if (node.element.compareTo(a) < 0 && node.element.compareTo(b) < 0){
            return lca(node.right, a, b);
        }
        return node;
    }

    /**
     * Balance the tree
     */
    public void balanceTree() {
        Vector<E> nodes = new Vector<>();
        toArray(root, nodes);
        root= balanceTree(nodes, 0, nodes.size() - 1);
    }

    private BinaryNode<E> balanceTree(Vector<E> elements, int low, int high){
        if (elements == null || low > high){
            return null;
        }
        int mid = (low + high) / 2;
        BinaryNode<E> node = new BinaryNode<>(elements.get(mid));
        node.left = balanceTree(elements, low, mid - 1);
        node.right = balanceTree(elements, mid + 1, high);
        return node;
    }

    private void toArray(BinaryNode<E> node, Vector<E> nodes){
        if (node == null){return;};
        toArray(node.left, nodes);
        nodes.add(node.element);
        toArray(node.right, nodes);
    }

    /**
     * In a BST, keep only nodes between range
     * This routine runs in O(n)
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
        root = keepRange(root, a, b);
    }
    private BinaryNode<E> keepRange(BinaryNode<E> node, E a, E b){
        if (node == null){return null;};
        node.left = keepRange(node.left, a, b);
        node.right = keepRange(node.right, a, b);

        if (node.element.compareTo(a) < 0) {
            BinaryNode<E> right = node.right;
            node = null;
            return right;
        }
        if (node.element.compareTo(b) > 0){
            BinaryNode<E> left = node.left;
            node = null;
            return left;
        }
        return node;
    }

    // Basic node stored in unbalanced binary  trees
    private static class BinaryNode<E> {
        E element;            // The data in the node
        BinaryNode<E> left;   // Left child
        BinaryNode<E> right;  // Right child

        // Constructors
        BinaryNode(E theElement) {
            this(theElement, null, null);
        }

        BinaryNode(E theElement, BinaryNode<E> lt, BinaryNode<E> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        // toString for BinaryNode
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node:");
            sb.append(element);
            return sb.toString();
        }

    }

}
