import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] flag; // whether sit is open, 1 open, 0 block.
    private int N;

    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException("Constructor N < 0");
        this.N = N;
        flag = new boolean[N * N + 2]; // initialize flag to 0;
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N * N + 2);
    }

    public boolean isOpen(int row, int col) {
        // row & col, 1 to N.
        validateIndex(row, col);
        return flag[xyTo1D(row - 1, col - 1)];
    }

    public boolean isFull(int row, int col) {
    }

    private int xyTo1D(int x, int y) {
        // x: row, 0 to N-1, y: column, 0 to N-1
        return x * N + y;
    }

    private
}
