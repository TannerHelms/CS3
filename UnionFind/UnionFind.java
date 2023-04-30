import java.util.ArrayList;
import java.util.Arrays;

public class UnionFind {
    private int size;
    private int[] elements;

    private int lastValue;

    public UnionFind(int size) {
        this.size = size;
        elements = new int[size * size + 2];
        Arrays.fill(elements, -1);
    }

    public void open(int x) {
        lastValue = x;
        // If element isn't empty then return
        if (elements[x] != -1) {
            return;
        }

        // Check if right side has value
        if (x % size != 0 && elements[x+1] != -1) {
            if (elements[x+1] == -999){
                elements[x+1] = -1;
                union(x, x+1);
            } else {
                union(x, x+ 1);
            }
        }
        // Check if x is on the left side before checking if it has a value to the left
        ArrayList<Integer> LeftSide = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            LeftSide.add((i - 1) * size + 1);
        }
        if (!LeftSide.contains(x) && elements[x-1] != -1) {
            if (elements[x-1] == -999){
                elements[x-1] = -1;
                union(x, x-1);
            } else {
                union(x, x-1);
            }
        }

        // Check for top neighbor
        if (x > size && elements[x - size] != -1) {
            if (elements[x-size] == -999) {
                elements[x-size] = -1;
                union(x-size, x);
            } else {
                union(x-size, x);
            }
        }

        // Check for bottom neighbor
        if (x <= (size * size) - size && elements[x+size] != -1) {
            if (elements[x + size] == -999) {
                elements[x+size] = -1;
                union(x+size, x);
            } else {
                union(x+size, x);
            }
        }
        // union if value is on the top
        if (x <= size) {
            union(x, 0);
        }

        // union if value is on the bottom
        else if (x > size * (size - 1)) {
            union(x, elements.length - 1);
        }

        if (elements[x] == -1) {
            elements[x] = -999;
        }
    }

    private void union (int root1, int root2){
        // Find the roots
        root1 = find(root1);
        root2 = find(root2);
        if (root1 == root2){
            return;
        }
        if (elements[root2] < elements[root1]) {
            elements[root1] = root2;
        } else {
            if (elements[root1] == elements[root2]) {
                elements[root1]--;
            }
            elements[root2] = root1;
        }
    }

    public int find(int x){
        if (elements[x] < 0) {
            return x;
        }
        return find(elements[x]);
    }

    ArrayList<Integer> visited = new ArrayList<>();
    public ArrayList<Integer> findPath() {
        findPath(lastValue);
        return visited;
    }
    private void findPath(int x) {
        if (visited.contains(x)) {
            return;
        }
        visited.add(x);
        if (elements[x] < 0) {
            return;
        }
        // Check if right side has value
        if (x % size != 0 && find(x) == find(x+1) && !visited.contains(x+1)) {
            findPath(x+1);
        }

        // Check if x is on the left side before checking if it has a value to the left
        ArrayList<Integer> LeftSide = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            LeftSide.add((i - 1) * size + 1);
        }
        if (!LeftSide.contains(x) && find(x) == find(x-1) && !visited.contains(x-1)) {
            findPath(x-1);
        }

        // Check for top neighbor
        if (x > size && find(x - size) == find(x) && !visited.contains(x-size)) {
            findPath(x - size);
        }

        // Check for bottom neighbor
        if (x <= (size * size) - size && find(x + size) == find(x) && !visited.contains(x+size)) {
            findPath(x+size);
        }
    }

    public boolean percolated() {
        if (find(0) == find(elements.length - 1)) {
            findPath();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder toStr = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            int row = (i - 1) * size + 1;
            toStr.append(toString(row, row + size - 1));
            toStr.append("\n");
        }
        return toStr.toString();
    }

    private String toString(int start, int end){
        if (start > end) {
            return "";
        }
        // Print green if its the last cell opened
        if (start == lastValue) {
            return "\033[0;102m x \u001B[0m" + toString(start + 1, end);
        }
        // Print red if its in the Percolated path
        if (find(0) == find(start) && visited.contains(start)) {
            return "\033[0;101m x \u001B[0m" + toString(start + 1, end);
        }
        // Print red if its in the Percolated path
        if (find(0) == find(start) && !percolated()) {
            return "\033[0;101m x \u001B[0m" + toString(start + 1, end);
        }
        // Print Black if its un opened
        if (elements[start] == -1) {
            // Black Print if its not opened
            return "\033[40m x \u001B[0m" + toString(start + 1, end);
        }

        // Print White if its open but no connections
        if (elements[start] == -999) {
            return "\033[0;107m x \u001B[0m"  + toString(start + 1, end);
        }

        // Print Blue opened but not unioned to the main
        return "\033[0;104m x \u001B[0m" + toString(start + 1, end);

    }
    public static void main(String[] args) {
        UnionFind unionFind = new UnionFind(3);
        unionFind.union(1,2);
        unionFind.union(7,8);
        unionFind.union(5,8);
        unionFind.union(2,5);
        String expectedOutput = "[-1, -3, 1, -1, -1, 7, -1, 1, 7, -1, -1]";
        String actualOutput = Arrays.toString(unionFind.elements);
        System.out.println(actualOutput);
        if (expectedOutput.equals(actualOutput)) {
            System.out.println("For Test 1, the strings match!");
        } else {
            System.out.println("For Test 1, the string do not match!");
        }

        unionFind = new UnionFind(3);
        unionFind.union(5,6);
        unionFind.union(7,8);
        unionFind.union(1,4);
        unionFind.union(4,5);
        expectedOutput = "[-1, -3, -1, -1, 1, 1, 5, -2, 7, -1, -1]";
        actualOutput = Arrays.toString(unionFind.elements);
        System.out.println(actualOutput);
        if (expectedOutput.equals(actualOutput)) {
            System.out.println("For Test 2, the strings match!");
        } else {
            System.out.println("For Test 2, the string do not match!");
        }

        unionFind = new UnionFind(3);
        unionFind.union(1,3);
        unionFind.union(7,9);
        unionFind.union(4,6);
        expectedOutput = "[-1, -2, -1, 1, -2, -1, 4, -2, -1, 7, -1]";
        actualOutput = Arrays.toString(unionFind.elements);
        System.out.println(actualOutput);
        if (expectedOutput.equals(actualOutput)) {
            System.out.println("For Test 3, the strings match!");
        } else {
            System.out.println("For Test 3, the string do not match!");
        }
    }
}
