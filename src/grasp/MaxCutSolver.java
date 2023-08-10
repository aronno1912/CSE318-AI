package grasp;
import java.util.*;
/**
 * the GRASP+PR-MAXCUT algorithm is an iterative optimization process that aims to find a high-quality solution for the Max-Cut problem.
 * It utilizes a combination of greedy construction, local search, and solution pool updating to explore the solution space and iteratively
 * improve the quality of the solutions.
 * The ultimate goal is to find a solution that satisfies certain criteria and maximizes the cut weight within the graph.
 *
 * **/
class Edge {
    int source;
    int destination;
    double weight;

    public Edge(int source, int destination, double weight)
    {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
}
public class MaxCutSolver {


    static boolean stoppingCriterionMet() {
        // Implement stopping criterion logic
        return false; // Change this condition accordingly
    }

    // Method to find the minimum edge weight in the weights array
    static double findMinWeight(double[][] weights) {
        double minWeight = Double.POSITIVE_INFINITY;
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                minWeight = Math.min(minWeight, weights[i][j]);
            }
        }
        return minWeight;
    }

    // Method to find the maximum edge weight in the weights array
    static double findMaxWeight(double[][] weights) {
        double maxWeight = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                maxWeight = Math.max(maxWeight, weights[i][j]);
            }
        }
        return maxWeight;
    }

    // Method to find the minimum value in the arrays within a subset of vertices
    static double findMin(double[] array, Set<Integer> subset) {
        double min = Double.POSITIVE_INFINITY;
        for (int vertex : subset) {
            min = Math.min(min, array[vertex]);
        }
        return min;
    }

    // Method to find the maximum value in the arrays within a subset of vertices
    static double findMax(double[] array, Set<Integer> subset) {
        double max = Double.NEGATIVE_INFINITY;
        for (int vertex : subset) {
            max = Math.max(max, array[vertex]);
        }
        return max;
    }

    /**
     *  an approach that combines elements of both greedy and randomized strategies.
     *  It's not purely greedy because it introduces randomness to the selection process.
     * **/

    static SolutionPair semiGreedyMaxCut(Graph graph, double[][] weights) {
        double alpha = Math.random(); // controls the balance between selecting vertices greedily and randomly. 0<alpha<1
        double Wmin = findMinWeight(weights);
        double Wmax = findMaxWeight(weights);
        //Calculate the threshold value meu
        //This threshold value helps decide whether an edge should be considered for selection based on its weight.
        double meu = Wmin + alpha * (Wmax - Wmin);
       //Create a Restricted Candidate List (RCLe) containing edges that have weights greater than or equal to the calculated meu.
        List<Edge> RCLe = new ArrayList<>();
        for (Edge edge : graph.edges) {
            if (weights[edge.source][edge.destination] >= meu) {
                RCLe.add(edge);
            }
        }
        //==========Random Edge Selection:===================
        Random random = new Random();
        Edge randomEdge = RCLe.get(random.nextInt(RCLe.size()));
        // X Y represents the two partitions being formed.
        Set <Integer> X = new HashSet <>();
        Set<Integer> Y = new HashSet<>();
        X.add(randomEdge.source);
        Y.add(randomEdge.destination);
        // 0  indexing
        Set<Integer> allVerticesSet = new HashSet<>();
        for (int i = 0; i < graph.numVertices; i++) {
            allVerticesSet.add(i);
        }

//        Set<Integer> union = new HashSet<>(X);
//        union.addAll(Y);
       //While the union of sets X and Y is not equal to the set of all vertices (V):
        // while ( (!X.addAll(Y) )|| ( X.size() < graph.numVertices))
       while ((X.size() + Y.size()) < graph.numVertices)
           {
            Set<Integer> VPrime = new HashSet<>(allVerticesSet);
            VPrime.removeAll(X);
            VPrime.removeAll(Y);

            double[] sigmaX = new double[graph.numVertices];
            double[] sigmaY = new double[graph.numVertices];

            // For each vertex v in the set VPrime, calculate the
            // cumulative edge weight sum sigmaX(v) and sigmaY(v) of edges from vertex v to vertices in sets X and Y, respectively.
               // mane v ekhono X ba Y set e add kori nai
            for (int v : VPrime)
            {
                for (int u : Y)  //cutting edge
                {
                    sigmaX[v] += weights[v][u];
                }
                for (int u : X)
                {
                    sigmaY[v] += weights[v][u];
                }
            }

            //double newWmin = findMin(sigmaX, sigmaY, VPrime);
            //Calculating new Thresold
            double newWminX = findMin(sigmaX, VPrime);
            double newWminY = findMin(sigmaY, VPrime);
            double newWmaxX = findMax(sigmaX, VPrime);
            double newWmaxY = findMax(sigmaY, VPrime);
            double newWmin=Math.min(newWminX,newWminY);
            double newWmax=Math.min(newWmaxX,newWmaxY);

            double newMeu = newWmin + alpha * (newWmax - newWmin);
            //Create a list RCLv of vertices from VPrime that have cumulative edge weights greater than or equal to the new threshold newMeu.
            List<Integer> RCLv = new ArrayList<>();
            for (int v : VPrime) {
                if (Math.max(sigmaX[v], sigmaY[v]) >= newMeu) {
                    RCLv.add(v);
                }
            }
           //=========== Random Vertex Selection: ==================
            int vStar = RCLv.get(random.nextInt(RCLv.size()));
            //This vertex is chosen to potentially be added to set X or Y.
               //jar cut weight beshi
            if (sigmaX[vStar] > sigmaY[vStar])
            {
                X.add(vStar);
            } else
            {
                Y.add(vStar);
            }
        }    // The loop iterates through these steps until either X and Y are not disjoint or the size of X reaches the total number of vertices in the graph.

        List<Integer> S = new ArrayList<>(X);
        List<Integer> SPrime = new ArrayList<>(Y);

        return new SolutionPair(S, SPrime,weights);
    }

    /**
     *  used to improve the quality of a solution (S, S') by iteratively swapping vertices
     *  between sets S and S'. The goal is to find a locally optimal solution within the neighborhood of the current solution.
     *
     *  introducing randomness to escape local optima and explore a wider solution space.
     * **/
    static SolutionPair localSearchMaxCut(SolutionPair initialSolution, double[][] weights) {
        boolean change = true;

        while (change) {
            change = false;

            for (int v = 0; v < weights.length; v++)
            {
                //sigmaS is the sum of edge weights from a definite vertex v to all vertices of opposite Set. that means if SigmaS >SigmaSPrime
                // it is better to keep the vertex v in the Set S, as cutting edges have more weight
                double sigmaS = initialSolution.getSigmaS(v);
                //System.out.println("sigmaS for "+v+" th vertex is "+sigmaS);
                double sigmaSPrime = initialSolution.getSigmaSPrime(v);
               // System.out.println("sigmaPrimeS for "+v+" th vertex is "+sigmaSPrime);
                if (initialSolution.isInS(v) && sigmaSPrime - sigmaS > 0)
                {
                    initialSolution.removeFromS(v);
                    initialSolution.addToSPrime(v);
                    change = true;
                }
                else if (initialSolution.isInSPrime(v) && sigmaS - sigmaSPrime > 0)  //greater than dewa
                {
                    initialSolution.removeFromSPrime(v);
                    initialSolution.addToS(v);
                    change = true;
                }
            }
        }

        return initialSolution;
    }

    static SolutionPair selectSolutionFromPool(List<SolutionPair> pool) {
        // Implement solution selection logic from the pool
        return null;
    }

    static SolutionPair pathRelinkingMaxCut(SolutionPair sourceSolution, SolutionPair targetSolution) {
        // Implement PATH-RELINKING-MAXCUT logic
        return null;
    }

    /**
     * The calculateCutWeight function is used to calculate the cut weight of a given solution pair (S, S') for the Max-Cut problem.
     * The cut weight is the sum of the weights of the edges that are "cut" (i.e., where one vertex is in set S and the other is in set S').
     * **/
    static double calculateCutWeight(SolutionPair solution, double[][] weights) {
        double cutWeight = 0.0;

        for (int v : solution.getS()) {
            for (int u : solution.getSPrime()) {
                cutWeight += weights[v][u];
            }
        }

        return cutWeight;
    }




    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of vertices (v): ");
        int v = scanner.nextInt();

        System.out.print("Enter the number of edges (e): ");
        int e = scanner.nextInt();

//        List<Edge> edges = new ArrayList<>();
//        System.out.println("Enter edge details (source destination weight):");
//        for (int i = 0; i < e; i++) {
//            int source = scanner.nextInt();
//            int destination = scanner.nextInt();
//            double weight = scanner.nextDouble();
//            edges.add(new Edge(source, destination, weight));
//        }
        // Construct the Graph object
        Graph graph = new Graph(v);

//        // Read and add edges with weights
//        for (int i = 0; i < e; i++) {
//            int source = scanner.nextInt();
//            int destination = scanner.nextInt();
//            double weight = scanner.nextDouble();
//            graph.addEdge(source, destination, weight);
//        }
//        System.out.println("Reading Done");
        // Construct the weights matrix
        double[][] weights = new double[v][v];
        for (int i = 0; i < e; i++) {
            int source = scanner.nextInt();
            int destination = scanner.nextInt();
            double weight = scanner.nextDouble();
            graph.addEdge(source, destination, weight);
            weights[source][destination] = weight;
            weights[destination][source] = weight; // an undirected graph
        }

        System.out.println("Construction and construction Done");
        List<Integer> SBest = new ArrayList<>();
        List<Integer> SBestPrime = new ArrayList<>();
        double wBest = Double.NEGATIVE_INFINITY;  //This variable will store the maximum cut weight found.
        List<SolutionPair> E = new ArrayList<>();  // will be used to store good solutions.

        // Define your stopping criterion
          int count=0;
          int sameResult=0;
        while (count<100)
        {
            // Invoke the SEMI-GREEDY-MAXCUT procedure to generate an initial solution (S, S')
            SolutionPair semiGreedySolution = semiGreedyMaxCut(graph,weights);

            //Improve the current solution (S, S') using the LOCAL-SEARCH-MAXCUT procedure.
            SolutionPair localSearchSolution = localSearchMaxCut(semiGreedySolution,weights);

            //If the set E is not empty (contains solutions from previous iterations):
            //Select a solution (Sg, Sg') from the pool E.
            //Perform PATH-RELINKING-MAXCUT between the current solution (S, S')
            //and the selected solution (Sg, Sg'). This step combines solutions to potentially find better ones.
//            SolutionPair pathRelinkingSolution = null;
//            if (!E.isEmpty())
//            {
//                SolutionPair selectedSolution = selectSolutionFromPool(E);
//                pathRelinkingSolution = pathRelinkingMaxCut(localSearchSolution, selectedSolution); // Implement PATH-RELINKING-MAXCUT
//            }
//
//            SolutionPair finalSolution = (pathRelinkingSolution != null) ? pathRelinkingSolution : localSearchSolution;
//
//            if (finalSolution.satisfiesMembershipConditions()) {
//                E.add(finalSolution);
//            }

            double w = calculateCutWeight(localSearchSolution,weights);
            if (w > wBest) {
                SBest = localSearchSolution.getS();
                SBestPrime = localSearchSolution.getSPrime();
                wBest = w;
            }
            else if(w==wBest)
                sameResult++;
            if(sameResult>15)
            {
                System.out.println("Continuously getting same result!!!!!!!!!!!!!!!!");
                break;
            }

            count++;
        }

        // Return the best solution and its weight
        System.out.println("Best Solution: " );
        System.out.println("S is "+SBest);
        System.out.println("S' is "+SBestPrime);
        System.out.println("Best Weight: " + wBest);

    }
}
