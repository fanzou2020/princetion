import edu.princeton.cs.algs4.Merge;

/**
 * Circular suffix array. To efficiently implement the key component in the Burrows–Wheeler transform,
 * you will use a fundamental data structure known as the circular suffix array, which describes the
 * abstraction of a sorted array of the n circular suffixes of a string of length n.
 */
public class CircularSuffixArray {
    private String s;
    private int[] index;

    // circular suffix array of s
    // use merge index-sort to solve this problem, very concise!!
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("argument of CircularSuffixArray constructor is null");
        this.s = s;
        CircularSuffix[] csArray = new CircularSuffix[s.length()];
        for (int i = 0; i < s.length(); i++) {
            csArray[i] = new CircularSuffix(s, i);
        }
        index = Merge.indexSort(csArray);
    }

    // inner class that represents a circular suffix implicitly (via a reference to the input
    // string and a pointer t the first character in the circular suffix)
    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private String s;
        private int indexOfFirstChar;
        private CircularSuffix(String s, int indexOfFirstChar) {
            this.s = s;
            this.indexOfFirstChar = indexOfFirstChar;
        }

        @Override
        public int compareTo(CircularSuffix o) {
            if (o == null) throw new IllegalArgumentException("argument of compareTo() is null");
            int i = this.indexOfFirstChar, j = o.indexOfFirstChar, n = s.length();
            for (int k = 0; k < n; k++) {
                if      (this.s.charAt(i) > o.s.charAt(j)) return 1;
                else if (this.s.charAt(i) < o.s.charAt(j)) return -1;
                else {
                    i = (i + 1) % n;
                    j = (j + 1) % n;
                }
            }
            return 0;
        }
    }


    // length of s
    public int length() { return s.length(); }

    // returns index of ith sorted suffix
    public int index(int i) { return index[i]; }

    // unit testing
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray cs = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            System.out.println(cs.index(i));
        }
        System.out.println("length = " + cs.length());
    }

}
