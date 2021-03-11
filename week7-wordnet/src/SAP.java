import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {

    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();

        Digraph defensiveGraphCopy = new Digraph(G);
        this.graph = defensiveGraphCopy;
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return sapAncestorOrLength(v, w, false);
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return sapAncestorOrLength(v, w, true);
    }


    private boolean validateVertexIndex(int vertex) {
        return vertex >= 0 && vertex < graph.V();
    }


    private boolean validateVertexIndex(Iterable<Integer> vertices) {
        for (int vertex : vertices) {
            if (vertex < 0 && vertex >= graph.V()) return false;
        }

        return true;
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        return sapAncestorOrLength(v, w, true);
    }


    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        return sapAncestorOrLength(v, w, false);
    }


    private int sapAncestorOrLength(int v, int w, boolean returnLength) {
        if (!validateVertexIndex(v) || !validateVertexIndex(w)) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);        
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);

        return sapFinder(bfsV, bfsW, returnLength);
    }


    private int sapAncestorOrLength(Iterable<Integer> v, Iterable<Integer> w, boolean returnLength) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (!validateVertexIndex(v) || !validateVertexIndex(w)) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);        
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);

        return sapFinder(bfsV, bfsW, returnLength);
    }


    private int sapFinder(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW, boolean returnLength) {
        int sapVertex = -1;
        int sapLength = Integer.MAX_VALUE;

        for (int i = 0; i < this.graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int currentPathLength = bfsV.distTo(i) + bfsW.distTo(i);
                if (currentPathLength < sapLength) {
                    sapVertex = i;
                    sapLength = currentPathLength;
                }
            }
        }

        if (sapVertex == -1) {
            return -1;
        } else {
            return returnLength ? sapLength : sapVertex;
        }
    }


    public static void main(String[] args) {
        String d1 = "digraph1.txt";
        String d2 = "digraph2.txt";
        String d3 = "digraph3.txt";
        // String digraphFileName = args[0];
        In in;
        Digraph graph;
        SAP sap;
        int shortestPathAncestor, shortestPath;
        Bag<Integer> v, w;
        
        // digraph 1 - construction
        in = new In(d1);
        graph = new Digraph(in);
        sap = new SAP(graph);

        // digraph 1 - ancestor of two vertices
        shortestPathAncestor = sap.ancestor(11, 12);
        assert shortestPathAncestor == 10;
        shortestPathAncestor = sap.ancestor(7, 8);
        assert shortestPathAncestor == 3;
        shortestPathAncestor = sap.ancestor(4, 8);
        assert shortestPathAncestor == 1;
        shortestPathAncestor = sap.ancestor(9, 8);
        assert shortestPathAncestor == 1;
        shortestPathAncestor = sap.ancestor(12, 8);
        assert shortestPathAncestor == 1;
        shortestPathAncestor = sap.ancestor(12, 2);
        assert shortestPathAncestor == 0;
        shortestPathAncestor = sap.ancestor(10, 5);
        assert shortestPathAncestor == 5;
        shortestPathAncestor = sap.ancestor(10, 10);
        assert shortestPathAncestor == 10;

        // digraph 1 - length of two vertices 
        shortestPath = sap.length(11, 12);
        assert shortestPath == 2;
        shortestPath = sap.length(7, 8);
        assert shortestPath == 2;
        shortestPath = sap.length(4, 8);
        assert shortestPath == 3;
        shortestPath = sap.length(9, 8);
        assert shortestPath == 4;
        shortestPath = sap.length(12, 8);
        assert shortestPath == 5;
        shortestPath = sap.length(12, 2);
        assert shortestPath == 5;
        shortestPath = sap.length(10, 5);
        assert shortestPath == 1;
        shortestPath = sap.length(5, 5);
        assert shortestPath == 0;

        // digraph 1 - ancestor of two sets of vertices
        v = new Bag<Integer>();
        v.add(10); 
        v.add(9);

        w = new Bag<Integer>();
        w.add(7); w.add(4);

        shortestPathAncestor = sap.ancestor(v, w);
        assert shortestPathAncestor == 1;
        shortestPath = sap.length(v, w);
        assert shortestPath == 3;

        // digraph 2 - construction
        in = new In(d2);
        graph = new Digraph(in);
        sap = new SAP(graph);

        // digraph2 - ancestor and length
        shortestPathAncestor = sap.ancestor(1, 2);
        shortestPath = sap.length(1, 2);
        assert shortestPath == 1;
        assert shortestPathAncestor == 2;

        shortestPathAncestor = sap.ancestor(1, 3);
        shortestPath = sap.length(1, 3);
        assert shortestPath == 2;
        assert shortestPathAncestor == 3;

        shortestPathAncestor = sap.ancestor(1, 4);
        shortestPath = sap.length(1, 4);
        assert shortestPath == 3;
        assert shortestPathAncestor == 0 || shortestPathAncestor == 4;

        shortestPathAncestor = sap.ancestor(1, 5);
        shortestPath = sap.length(1, 5);
        assert shortestPath == 2;
        assert shortestPathAncestor == 0;

        // digraph 2 - ancestor of two sets of vertices
        v = new Bag<Integer>();
        v.add(1); v.add(0);

        w = new Bag<Integer>();
        w.add(3); w.add(4);

        shortestPathAncestor = sap.ancestor(v, w);
        assert shortestPathAncestor == 3 || shortestPathAncestor == 0;
        shortestPath = sap.length(v, w);
        assert shortestPath == 2;

        // digraph 3 - construction
        in = new In(d3);
        graph = new Digraph(in);
        sap = new SAP(graph);
        
        // digraph 3 - length and ancestor
        shortestPathAncestor = sap.ancestor(1, 4);
        shortestPath = sap.length(1, 4);
        assert shortestPath == 3;
        assert shortestPathAncestor == 4 || shortestPathAncestor == 1;

        shortestPathAncestor = sap.ancestor(1, 5);
        shortestPath = sap.length(1, 5);
        assert shortestPathAncestor == 1;
        assert shortestPath == 2;

        shortestPathAncestor = sap.ancestor(7, 13);
        shortestPath = sap.length(7, 13);
        assert shortestPathAncestor == 8;
        assert shortestPath == 6;

        shortestPathAncestor = sap.ancestor(7, 1);
        shortestPath = sap.length(7, 1);
        assert shortestPathAncestor == -1;
        assert shortestPath == -1;

        shortestPathAncestor = sap.ancestor(8, 12);
        shortestPath = sap.length(8, 12);
        assert shortestPathAncestor == 8;
        assert shortestPath == 1;
    }
}
