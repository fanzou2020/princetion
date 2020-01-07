import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class SparseVector {
    private HashMap<Integer, Double> v;  // Use Hash Symbol Tables (HashST), because order not important

    public SparseVector() { v = new HashMap<>(); }

    public void put(int i, double x) { v.put(i, x); }

    public double get(int i) {
        if (!v.containsKey(i)) return 0.0;
        else return v.get(i);
    }

    public Iterable<Integer> indices() {
        // return all nonzero indices
        return v.keySet();
    }

    public double dot(double[] that) {
        double sum = 0.0;
        for (int i : indices())
            sum += that[i]*this.get(i);
        return sum;
    }

    public static void main(String[] args) {
        double[] a = {1, 0, 0, 0, 0, 1, 0, 2};
        SparseVector sv = new SparseVector();
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) sv.put(i, a[i]);
        }

        double[] b = {1, 1, 1, 1, 1, 1, 1 ,1};
        StdOut.println(sv.dot(b));
    }
}
