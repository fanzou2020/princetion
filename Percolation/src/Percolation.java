import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] flag; // whether sit is open, 1 open, 0 block.
    private final int n;
    private int numberOfOpenSites = 0;
    private final WeightedQuickUnionUF uf, uf2;
    private final int virtualTopSite, virtualBottomSite;

    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException("Constructor N < 0");
        this.n = N;
        flag = new boolean[N * N + 2]; // initialize flag to 0; introduce the virtual top site
        uf = new WeightedQuickUnionUF(N * N + 2);
        uf2 = new WeightedQuickUnionUF(N * N + 1);
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
        return isOpen(row, col) && uf2.connected(xyTo1D(row, col), virtualTopSite);
    }

    public boolean percolates() { return uf.connected(virtualTopSite, virtualBottomSite); }

    public int numberOfOpenSites() { return numberOfOpenSites; }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndex(row, col);
        int index = xyTo1D(row, col);
        if (!flag[index]) {
            flag[index] = true;
            numberOfOpenSites++;
        }
        if (!isLeftEdge(row, col)) {
            if (isOpen(row, col-1)) union(index, xyTo1D(row, col-1));
        }

        if (!isRightEdge(row, col)) {
            if (isOpen(row, col+1)) union(index, xyTo1D(row, col+1));
        }

        if (!isTopEdge(row, col)) {
            if (isOpen(row-1, col)) union(index, xyTo1D(row-1, col));
        }
        else { // isTopEdge, connect to virtual top site.
            union(index, virtualTopSite);
            flag[virtualTopSite] = true;
        }

        if (!isBottomEdge(row, col)) {
            if (isOpen(row+1, col)) union(index, xyTo1D(row+1, col));
        }
        else { // is BottomEdge, union to bottom virtual site
            uf.union(index, virtualBottomSite);
            flag[virtualBottomSite] = true;
        }
    }

    private void union(int p, int q) {
        uf.union(p, q);
        uf2.union(p, q);
    }

    // convert the row # and col # to array index
    private int xyTo1D(int row, int col) {
        return (row-1) * n + (col-1);
    }

    private boolean isValid(int x, int y) {
        return x >= 1 && x <= n && y >= 1 && y <= n;
    }
    private void validateIndex(int x, int y) {
        if (!isValid(x, y)) throw new IllegalArgumentException(String.format("N:%d, x:%d, y:%d", n, x, y));
    }

    private boolean isLeftEdge(int row, int col) { return col == 1; }
    private boolean isRightEdge(int row, int col) { return col == n; }
    private boolean isTopEdge(int row, int col) { return row == 1; }
    private boolean isBottomEdge(int row, int col) { return row == n; }

    public static void main(String[] args) {
            Percolation p = new Percolation(3);
            System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));
            p.open(1, 2);
            System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));


            System.out.println("p.isOpen(2,2) = " + p.isOpen(2, 2));
            p.open(2, 2);
            System.out.println("p.isOpen(2,2) = " + p.isOpen(2, 2));
            System.out.println("p.isFull(2, 2) = " + p.isFull(2, 2));


            System.out.println("p.isOpen(3, 1) = " + p.isOpen(3, 1));
            p.open(3, 1);
            System.out.println("p.isOpen(3, 1) = " + p.isOpen(3, 1));
            System.out.println("p.isFull(3,1) = " + p.isFull(3, 1));


            System.out.println("p.percolates() = " + p.percolates());
        }


}
