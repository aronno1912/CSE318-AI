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
    private double[][] weights; // Add this if weights are needed for calculation

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

    public double getSigmaS(int vertex) {
        double sigmaS = 0.0;
        for (int u : S) {
            sigmaS += weights[vertex][u];
        }
        return sigmaS;
    }

    public boolean isInS(int vertex) {
        return S.contains(vertex);
    }

    public boolean isInSPrime(int vertex) {
        return SPrime.contains(vertex);
    }

    public double getSigmaSPrime(int vertex) {
        double sigmaSPrime = 0.0;
        for (int u : SPrime) {
            sigmaSPrime += weights[vertex][u];
        }
        return sigmaSPrime;
    }

    public boolean satisfiesMembershipConditions() {
        // Implement membership condition logic if needed
        return true; // Placeholder
    }
}
