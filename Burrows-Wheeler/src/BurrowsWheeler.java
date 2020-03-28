import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

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
        int first = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();
        int n = t.length;
        int[] next = new int[n];
        // key-index counting algorithm to construct next[] array
        int[] count = new int[R+1];  // count array has one more element than R.
        // 1. count frequency
        for (int i = 0; i < n; i++)
            count[t[i]+1]++;

        // 2. compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

        // 3. construct next[] array
        for (int i = 0; i < n; i++)
            next[count[t[i]]++] = i;

        // reconstruct the original input string using next[] and t[]
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(t[next[first]]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0] == null) throw new IllegalArgumentException("args[0] is null");
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
