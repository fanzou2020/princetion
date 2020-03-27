import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray cs = new CircularSuffixArray(s);

        int i;
        for (i = 0; i < s.length(); i++) {
            if (cs.index(i) == 0) break;
        }
        BinaryStdOut.write(i);

        for (int j = 0; j < s.length(); j++) {
            int lastIndex = (cs.index(j) + s.length() - 1) % s.length();
            BinaryStdOut.write(s.charAt(lastIndex));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0] == null) throw new IllegalArgumentException("args[0] is null");
        if (args[0].equals("-")) transform();
    }
}
