import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] mtf = new char[R];           // move-to-front array
        for (int r = 0; r < R; r++)
            mtf[r] = (char) r;

        while (!BinaryStdIn.isEmpty()) {
            // read a char
            char c = BinaryStdIn.readChar();

            // find index
            int index;
            for (index = 0; index < R; index++) {
                if (mtf[index] == c) break;
            }
            BinaryStdOut.write(index, 8);

            // move to front
            for (int i = index-1; i >= 0; i--)
                mtf[i+1] = mtf[i];
            mtf[0] = c;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] mtf = new char[R];
        for (int r = 0; r < R; r++)
            mtf[r] = (char) r;

        while (!BinaryStdIn.isEmpty()) {
            // read a char
            char c = BinaryStdIn.readChar();
            char out = mtf[c];

            // write to output
            BinaryStdOut.write(mtf[c]);

            // move to front
            for (int i = c-1; i >= 0; i--)
                mtf[i+1] = mtf[i];
            mtf[0] = out;
        }
        BinaryStdOut.close();
    }


    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0] == null) throw new IllegalArgumentException("args[0] is null");
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
