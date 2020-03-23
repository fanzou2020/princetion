import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 *  The {@code RunLength} class provides static methods for compressing
 *  and expanding a binary input using run-length coding with 8-bit
 *  run lengths.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class RunLength {
    private static final int R = 256;
    private static final int LG_R = 8;

    // Do not instantiate.
    private RunLength() { }

    /**
     * Reads a sequence of bits from standard input (that are encoded
     * using run-length encoding with 8-bit run lengths); decodes them;
     * and writes the results to standard output.
     */
    public static void expand() {
        boolean b = false;
        while (!BinaryStdIn.isEmpty()) {
            int run = BinaryStdIn.readInt(LG_R);
            for (int i = 0; i < run; i++)
                BinaryStdOut.write(b);
            b = !b;
        }
        BinaryStdOut.close();
    }

    /**
     * 1. Read a bit
     * 2. If it differs from the last bit read, write the current count and reset count to 0
     * 3. If it is the same as the last bit read, and the count is a maximum, write the count,
     * write a 0 count, and reset the count to 0.
     */
    public static void compress() {
        char cnt = 0;
        boolean old = false;
        while (!BinaryStdIn.isEmpty()) {
            boolean b = BinaryStdIn.readBoolean();
            if (b != old) {
                BinaryStdOut.write(cnt, LG_R);
                cnt = 0;
                old = !old;
            }
            else {
                if (cnt == R-1) {
                    BinaryStdOut.write(cnt, LG_R);
                    cnt = 0;
                    BinaryStdOut.write(cnt, LG_R);
                }
            }
            cnt++;
        }
        BinaryStdOut.write(cnt);
        BinaryStdOut.close();
    }

    /**
     *
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
