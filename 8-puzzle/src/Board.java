import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public final class Board {
    private final int[][] blocks;   // array of blocks
    private final int n;            // board dimension n

    // Constructor
    public Board(int[][] blocks) {
        this.n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];  // defensive copy of mutable instance variables
            }
        }
    }

    public int dimension() {
        return n;
    }

    /**
     * check whether the block is the goal block.
     * @return true if isGoal.
     */
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            if (i != n - 1) {
                for (int j = 0; j < n; j++) {
                    if (blocks[i][j] != i * n + j + 1) return false;
                }
            }
            else {
                for (int j = 0; j < n - 1; j++) {
                    if (blocks[i][j] != i * n + j + 1) return false;
                }
                if (blocks[n - 1][n - 1] != 0) return false;
            }
        }
        return true;
    }

    /**
     * @return the sum of Manhattan distance between blocks and goal.
     */
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) continue;
                int rowOfGoal = (blocks[i][j] - 1) / n;
                int colOfGoal = (blocks[i][j] - 1) % n;
                int distance = Math.abs(rowOfGoal - i) + Math.abs(colOfGoal - j);
                sum += distance;
            }
        }
        return sum;
    }

    /**
     * @return the number of blocks out of place.
     */
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) continue;
                if (blocks[i][j] != i*n + j+1) sum++;
            }
        }
        return sum;
    }

    /**
     * exchange the first two elements if they are neither zeros.
     * @return
     */
    public Board twin() {
        int[][] tempBlocks;
        tempBlocks = copy(blocks);

        // if tempBlocks[0][0] == 0 or tempBlocks[0][1] == 0,
        // swap [1][0] & [1][1]
        if (tempBlocks[0][0] == 0 || tempBlocks[0][1] == 0) {
            exch(tempBlocks, 1, 0, 1, 1);
        }
        // else, swap blocks[0][0] & blocks[0][1].
        else {
            exch(tempBlocks, 0, 0, 0, 1);
        }
        return new Board(tempBlocks);
    }

    /**
     * exchange a[i][j] with a[m][n]
     */
    private void exch(int[][] a, int i, int j, int k, int m) {
        int temp = a[i][j];
        a[i][j] = a[k][m];
        a[k][m] = temp;
    }

    /**
     * @param  blocks
     * @return the defensive copy of block a.
     */
    private int[][] copy(int[][] b) {
        int[][] copyOfBlocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copyOfBlocks[i][j] = b[i][j];
            }
        }
        return copyOfBlocks;
    }

    /**
     * Compares this board to board y.
     * @param y that board.
     * @return true if two board equal.
     */
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (Arrays.deepEquals(this.blocks, that.blocks)) && (this.n == that.n);
    }

    /**
     * all neighboring boards
     * @return stack of all neighboring boards.
     */
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        int row0 = 0, col0 = 0;

        // find the row and column number of blank space.
        lableA: // loop tag
        for (int i = 0; i < n; i++) {
            lableB:
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                    break lableA;
                }
            }
        }

        // exchange blank space a[i,j] with a[i, j-1]
        if ((col0-1) >= 0 && (col0-1) < n) {
            int[][] neighborBlock1;
            neighborBlock1 = copy(blocks);
            exch(neighborBlock1, row0, col0, row0, col0-1);
            Board neighbor1 = new Board(neighborBlock1);
            stack.push(neighbor1);
        }

        // exchange blank space a[i,j] with a[i, j+1]
        if ((col0+1) < n) {
            int[][] neighborBlock2;
            neighborBlock2 = copy(blocks);
            exch(neighborBlock2, row0, col0, row0, col0+1);
            Board neighbor2 = new Board(neighborBlock2);
            stack.push(neighbor2);
        }

        // exchange blank space a[i,j] with a[i-1, j]
        if ((row0-1) >= 0 && (row0-1) < n) {
            int[][] neighborBlock3;
            neighborBlock3 = copy(blocks);
            exch(neighborBlock3, row0, col0, row0-1, col0);
            Board neighbor3 = new Board(neighborBlock3);
            stack.push(neighbor3);
        }

        // exchange blank space a[i,j] with a[i+1, j]
        if ((row0+1) < n) {
            int[][] neighborBlock4;
            neighborBlock4 = copy(blocks);
            exch(neighborBlock4, row0, col0, row0+1, col0);
            Board neighbor4 = new Board(neighborBlock4);
            stack.push(neighbor4);
        }

        return stack;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] b = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                b[i][j] = in.readInt();
        Board initial = new Board(b);

        Iterable<Board> stack;
        stack = initial.neighbors();

        StdOut.println(initial);
        for (Board a: stack) {
            StdOut.println(a);
        }

        StdOut.println(initial.isGoal());
        StdOut.println(initial.twin());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());



    }

}
