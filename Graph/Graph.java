import java.io.File;
import java.util.*;

public class Graph {
    private int vertexCt;  // Number of vertices in the graph.
    private int[][] capacity;  // Adjacency  matrix
    private int[][] residual; // residual matrix
    private int[][] edgeCost; // cost of edges in the matrix
    private int[] cost;

    private int[] pred;
    private String graphName;  //The file from which the graph was created.
    private int totalFlow; // total achieved flow
    private int source = 0; // start of all paths
    private int sink; // end of all paths

    public Graph(String fileName) {
        this.vertexCt = 0;
        source  = 0;
        this.graphName = "";
        makeGraph(fileName);

    }

    /**
     * Method to add an edge
     *
     * @param source      start of edge
     * @param destination end of edge
     * @param cap         capacity of edge
     * @param weight      weight of edge, if any
     * @return edge created
     */
    private boolean addEdge(int source, int destination, int cap, int weight) {
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        capacity[source][destination] = cap;
        residual[source][destination] = cap;
        edgeCost[source][destination] = weight;
        edgeCost[destination][source] = -weight;
        return true;
    }

    /**
     * Method to get a visual of the graph
     *
     * @return the visual
     */
    public String printMatrix(String label, int[][] m) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n " + label+ " \n     ");
        for (int i=0; i < vertexCt; i++)
            sb.append(String.format("%5d", i));
        sb.append("\n");
        for (int i = 0; i < vertexCt; i++) {
            sb.append(String.format("%5d",i));
            for (int j = 0; j < vertexCt; j++) {
                sb.append(String.format("%5d",m[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Method to make the graph
     *
     * @param filename of file containing data
     */
    private void makeGraph(String filename) {
        try {
            graphName = filename;
            System.out.println("\n****Find Flow " + filename);
            Scanner reader = new Scanner(new File(filename));
            vertexCt = reader.nextInt();
            capacity = new int[vertexCt][vertexCt];
            residual = new int[vertexCt][vertexCt];
            edgeCost = new int[vertexCt][vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                for (int j = 0; j < vertexCt; j++) {
                    capacity[i][j] = 0;
                    residual[i][j] = 0;
                    edgeCost[i][j] = 0;
                }
            }

            // If weights, need to grab them from file
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                int weight = reader.nextInt();
                if (!addEdge(v1, v2, cap, weight))
                    throw new Exception();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sink = vertexCt - 1;
        System.out.println( printMatrix("Edge Cost" ,edgeCost));
    }


    public void minCostMaxFlow(){
        System.out.println( printMatrix("Capacity", capacity));
        //findWeightedFlow();
        System.out.println(printMatrix("Residual", residual));
        //finalEdgeFlow();
    }

    private boolean hasAugmentingCheapestPath() {
        cost = new int[vertexCt];
        pred = new int[vertexCt];
        for (int i = 0; i < vertexCt; i++) {
            cost[i] = 999;
            pred[i] = -1;
        }
        cost[0] = 0;
        for (int i = 0; i < vertexCt; i++) {
            for (int u = 0; u < vertexCt; u++) {
                for (int v = 0; v < vertexCt; v++) {
                    if (residual[u][v] != 0 && cost[v] > cost[u] + edgeCost[u][v]) {
                        cost[v] = cost[u] + edgeCost[u][v];
                        pred[v] = u;
                    }
                }
            }
        }
        return pred[sink] != -1;
    }

    public void FordFulkerson() {
        Hashtable<Integer, Double> flow = new Hashtable<>();
        System.out.println("Paths found in order");
        while (hasAugmentingCheapestPath()) {
            double avilFlow = 999;
            StringBuilder path = new StringBuilder();
            for (int v = vertexCt - 1; v != 0; v=pred[v]) {
                int prev = pred[v];
                avilFlow = Math.min(avilFlow, residual[prev][v]);
            }
            int cost = 0;
            int value = 0;
            for (int v = vertexCt - 1; v !=  0; v=pred[v]) {
                path.insert(0, v + " ");
                int prev = pred[v];
                cost += edgeCost[prev][v];
                residual[prev][v] -= avilFlow;
                residual[v][prev] += avilFlow;
                value = v;
            }
            path.insert(0, 0+ " ");
            path.append("(").append(avilFlow).append(")").append(" $ ").append(cost);
            System.out.println(path);
            flow.put(value, avilFlow);
        }
        System.out.println("Final flow on each edge");
        // Figure out the first set of nodes
        ArrayList<Integer> firstNodes = new ArrayList<>();
        for (int i = 0; i < vertexCt; i++) {
            if (capacity[0][i] != 0) {
                firstNodes.add(i);
            }
        }
        int ct = 1;
        for (int node: firstNodes) {
            System.out.print("Flow 0");
            int cost = findSuccessor(node, edgeCost[0][node]);
            System.out.print(" -> " + (vertexCt - 1));
            System.out.print("(" + flow.get(node) + ")");
            System.out.print(" $ " + cost);
            System.out.println();
        }
    }

    private int findSuccessor(int s, int cost) {
        for (int i = 0; i < vertexCt; i++) {
            if (capacity[s][i] != 0 && capacity[s][i] != residual[s][i]) {
                System.out.print(" -> " + s);
                cost += edgeCost[s][i];
                return findSuccessor(i,  cost);
            }
        }
        return cost;
    }


    public static void main(String[] args) {
        String[] files = {"match0.txt", "match1.txt", "match2.txt", "match3.txt", "match4.txt", "match5.txt","match6.txt", "match7.txt", "match8.txt", "match9.txt"};
        for (String fileName : files) {
            Graph graph = new Graph(fileName);
            graph.minCostMaxFlow();
            graph.FordFulkerson();
        }
    }
}