import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.TST;

/**
 *  The {@code LZW} class provides static methods for compressing
 *  and expanding a binary input using LZW compression over the 8-bit extended
 *  ASCII alphabet with 12-bit codewords.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class LZW {
    private static final int R = 256;       // number of input chars
    private static final int L = 4096;      // number of codewords = 2^W
    private static final int W = 12;        // codeword width

    // Do not instantiate.
    private LZW() { }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compress them using LZW compression
     * with 12-bit codewords; and writes the results to standard output.
     */
    public static void compress() {
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<>();

        // First initialize symbol tables with R possible single-character string keys
        // and associate them with W-bit codewords
        for (int i = 0; i < R; i++) {
            st.put("" + (char) i, i);  // e.g. 'A' = 41
        }
        int code = R + 1;                  // R is codeword for EOF.

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);       // Find max prefix match.
            BinaryStdOut.write(st.get(s), W);           // Print s's encoding.
            int t = s.length();                         // length of longest prefix match
            if (t < input.length() && code < L)         // look ahead and append the next character to s
                st.put(input.substring(0, t+1), code++);// then add to symbol table.
            input = input.substring(t);                 // Scan past s in input.
        }
        BinaryStdOut.write(R, W);   // Write EOF.
        BinaryStdOut.close();
    }


    /**
     * Reads a sequence of bit encoded using LZW compression with 12-bit codewords from
     * standard input; expand them; and writes the results to standard output.
     */
    public static void expand() {
        String[] st = new String[L];                // Array of symbol tables.
        int i;  // next available codeword value

        for (i = 0; i < R; i++) {                   // Initialize table for single chars.
            st[i] = "" + (char) i;
        }
        st[i++] = " ";      // (unused) lookahead for EOF.

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];
    }
}
