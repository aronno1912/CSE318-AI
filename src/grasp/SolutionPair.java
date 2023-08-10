package grasp;

import java.util.List;

//class SolutionPair {
//    private List <Integer> S;
//    private List<Integer> SPrime;
//
//    public SolutionPair(List<Integer> S, List<Integer> SPrime) {
//        this.S = S;
//        this.SPrime = SPrime;
//    }
//
//    public List<Integer> getS() {
//        return S;
//    }
//
//    public List<Integer> getSPrime() {
//        return SPrime;
//    }
//
//    public boolean satisfiesMembershipConditions() {
//        // Implement membership condition logic
//        return true; // Placeholder
//    }
//}

import java.util.List;

class SolutionPair {
    private List<Integer> S;
    private List<Integer> SPrime;
    private double[][] weights;

    public SolutionPair(List<Integer> S, List<Integer> SPrime, double[][] weights) {
        this.S = S;
        this.SPrime = SPrime;
        this.weights = weights;
    }

    public List<Integer> getS() {
        return S;
    }

    public List<Integer> getSPrime() {
        return SPrime;
    }

    public void addToS(int vertex) {
        S.add(vertex);
    }

    public void removeFromS(int vertex) {
        S.remove((Integer) vertex);
    }

    public void addToSPrime(int vertex) {
        SPrime.add(vertex);
    }

    public void removeFromSPrime(int vertex) {
        SPrime.remove((Integer) vertex);
    }

    //calculates the sum of the weights of the edges connecting the specified vertex with all the vertices in the set SPrime of a solution pair
    //This is an essential component in determining whether moving the vertex to the other set (S') could lead to a better cut weight.
    public double getSigmaS(int vertex)
    {
        double sigmaS = 0.0;
        for (int u : SPrime) {
            sigmaS += weights[vertex][u];
        }
        return sigmaS;
    }

    public double getSigmaSPrime(int vertex) {
        double sigmaSPrime = 0.0;
        for (int u : S) {
            sigmaSPrime += weights[vertex][u];
        }
        return sigmaSPrime;
    }


    public boolean isInS(int vertex) {
        return S.contains(vertex);
    }

    public boolean isInSPrime(int vertex) {
        return SPrime.contains(vertex);
    }


    public boolean satisfiesMembershipConditions() {
        // Implement membership condition logic if needed
        return true; // Placeholder
    }
}
