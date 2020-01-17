import edu.princeton.cs.algs4.*;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("SAP(Digraph G) is null");
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v >= G.V() || w >= G.V()) throw new IllegalArgumentException("length arguments out of bound");
        return find(v, w)[0];
    }
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("length arguments are null");
        return find(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v >= G.V() || w >= G.V()) throw new IllegalArgumentException("length arguments out of bound");
        return find(v, w)[1];
    }
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("length arguments are null");
        return find(v, w)[1];
    }

    // find the shortest ancestor path with a common ancestor
    private int[] find(int v, int w) {
        int nV = G.V(), nE = G.E();
        boolean[] hasPathToV = new boolean[nV];
        boolean[] hasPathToW = new boolean[nV];
        int[] distToV = new int[nV];
        int[] distToW = new int[nV];

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < nV; i++) {
            // for each vertex i, whether they have path to v and w, and their distance to v and w.
            hasPathToV[i] = bfsV.hasPathTo(i);
            hasPathToW[i] = bfsW.hasPathTo(i);
            distToV[i] = bfsV.distTo(i);
            distToW[i] = bfsW.distTo(i);
        }

        // find the minimum length of (v-->i + w-->i)
        int minLength = Integer.MAX_VALUE;
        int minAncestor = -1;
        for (int i = 0; i < nV; i++) {
            if (hasPathToV[i] && hasPathToW[i]) {
                int length = distToV[i] + distToW[i];
                if (length < minLength) {
                    minLength =length;
                    minAncestor = i;
                }
            }
        }
        if (minAncestor == -1) minLength = -1;
        return new int[] {minLength, minAncestor};
    }

    private int[] find(Iterable<Integer> v, Iterable<Integer> w) {
        int nV = G.V(), nE = G.E();
        boolean[] hasPathToV = new boolean[nV];
        boolean[] hasPathToW = new boolean[nV];
        int[] distToV = new int[nV];
        int[] distToW = new int[nV];

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < nV; i++) {
            // for each vertex i, whether they have path to v and w, and their distance to v and w.
            hasPathToV[i] = bfsV.hasPathTo(i);
            hasPathToW[i] = bfsW.hasPathTo(i);
            distToV[i] = bfsV.distTo(i);
            distToW[i] = bfsW.distTo(i);
        }

        // find the minimum length of (v-->i + w-->i)
        int minLength = Integer.MAX_VALUE;
        int minAncestor = -1;
        for (int i = 0; i < nV; i++) {
            if (hasPathToV[i] && hasPathToW[i]) {
                int length = distToV[i] + distToW[i];
                if (length < minLength) {
                    minLength =length;
                    minAncestor = i;
                }
            }
        }
        if (minAncestor == -1) minLength = -1;
        return new int[] {minLength, minAncestor};
    }

    // do unit
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }



}
