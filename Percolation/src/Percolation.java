import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] flag; // whether sit is open, 1 open, 0 block.
    private int N, numberOfOpenSites = 0;
    private WeightedQuickUnionUF uf;
    private final int virtualTopSite, virtualBottomSite;

    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException("Constructor N < 0");
        this.N = N;
        flag = new boolean[N * N + 2]; // initialize flag to 0; introduce the virtual top site
        this.uf = new WeightedQuickUnionUF(N * N + 2);
        virtualTopSite = N * N;
        virtualBottomSite = N * N + 1;
    }

    public boolean isOpen(int row, int col) {
        // row & col, 1 to N.
        validateIndex(row, col);
        return flag[xyTo1D(row, col)];
    }

    public boolean isFull(int row, int col) {
        validateIndex(row, col);
        return uf.connected(xyTo1D(row, col), virtualTopSite);
    }

    public boolean percolates() { return uf.connected(virtualTopSite, virtualBottomSite); }

    public int numberOfOpenSites() { return numberOfOpenSites; }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndex(row, col);
        int index = xyTo1D(row, col);
        flag[index] = true;
        numberOfOpenSites++;
        if (!isLeftEdge(row, col)) {
            if (isOpen(row, col-1)) uf.union(index, xyTo1D(row, col-1));
        }

        if (!isRightEdge(row, col)) {
            if (isOpen(row, col+1)) uf.union(index, xyTo1D(row, col+1));
        }

        if (!isTopEdge(row, col)) {
            if (isOpen(row-1, col)) uf.union(index, xyTo1D(row-1, col));
        }
        else { // isTopEdge, connect to virtual top site.
            uf.union(index, virtualTopSite);
            flag[virtualTopSite] = true;
        }

        if (!isBottomEdge(row, col)) {
            if (isOpen(row+1, col)) uf.union(index, xyTo1D(row+1, col));
        }
        else { // is BottomEdge, union to bottom virtual site
            uf.union(index, virtualBottomSite);
            flag[virtualBottomSite] = true;
        }
    }

    // convert the row # and col # to array index
    private int xyTo1D(int row, int col) {
        return (row-1) * N + (col-1);
    }

    private boolean isValid(int x, int y) {
        return x >= 1 && x <= N && y >= 1 && y <= N;
    }
    private void validateIndex(int x, int y) {
        if (!isValid(x, y)) throw new IndexOutOfBoundsException(String.format("N:%d, x:%d, y:%d", N, x, y));
    }

    private boolean isLeftEdge(int row, int col) { return col == 1; }
    private boolean isRightEdge(int row, int col) { return col == N; }
    private boolean isTopEdge(int row, int col) { return row == 1; }
    private boolean isBottomEdge(int row, int col) { return row == N; }

}
