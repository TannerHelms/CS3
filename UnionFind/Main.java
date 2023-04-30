import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int size = 20;
        UnionFind unionFind = new UnionFind(size);
        int count = 0;
        ArrayList<Integer> randomValues = new ArrayList<>();
        while (!unionFind.percolated()) {
            int random = (int) (Math.random() * size * size - 1) + 1;
            // Print out boards
            if (randomValues.contains(random)){
                continue;
            }
            if (count == 50) {
                System.out.println("50 open sites\n" + unionFind);
            }
            if (count == 100) {
                System.out.println("100 open sites\n" + unionFind);
            }
            if (count == 150) {
                System.out.println("150 open sites\n" + unionFind);
            }

            randomValues.add(random);
            unionFind.open(random);
            count++;
        }
        System.out.println(count + " open sites");
        System.out.println(unionFind);

    }
}