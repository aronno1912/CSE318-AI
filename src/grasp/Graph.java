package grasp;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    int numVertices;
    List<Edge> edges;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int source, int destination,double weight) {
        edges.add(new Edge(source, destination,weight));
    }

    // Other methods and properties specific to your graph representation
}
