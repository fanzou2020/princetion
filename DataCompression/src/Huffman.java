import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;

/**
 *  The {@code Huffman} class provides static methods for compressing
 *  and expanding a binary input using Huffman codes over the 8-bit extended
 *  ASCII alphabet.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Huffman {

    // alphabet size of extended ASCII
    private static final int R = 256;

    // Do not instantiate.
    private Huffman() { }

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node (char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // is the node a leaf node?
        public boolean isLeaf() {
            return left == null && right == null;
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }


    /**
     * Reads a sequence of bits that represents a Huffman-compressed message from
     * standard input; expands them; and writes the results to standard output.
     */
    public static void expand() {
        // read in Huffman trie from input stream
        Node root = readTrie();

        // number of bytes to write
        int length = BinaryStdIn.readInt();

        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                if (BinaryStdIn.readBoolean())
                    x = x.right;
                else
                    x = x.left;
            }
            BinaryStdOut.write(x.ch, 8);  // x is the leaf, output the char.
        }
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses them using Huffman codes with
     * an 8-bit alphabet; and writes the results to standard output.
     */
    public static void compress() {
        // read the input
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] st = new String[R];
        buildCode(st, root, "");

        // print trie for decoder
        writeTrie(root);

        // print number of bytes in original uncompressed message
        BinaryStdOut.write(input.length);

        // use Huffman code to encode input
        for (int i = 0; i < input.length; i++) {        // for every char in input stream
            String code = st[input[i]];                 // binary code for this char
            for (int j = 0; j < code.length(); j++) {   // transform character to binary code
                if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                }
                else if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                }
                else throw new IllegalStateException("Illegal state");
            }
        }

        // close output stream
        BinaryStdOut.close();
    }


    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + "0");     // if not leaf node, go to the left subtree, add 0 to s.
            buildCode(st, x.right, s + "1");    // then go to the right subtree, add 1 to s.
        }
        else {
            st[x.ch] = s;
        }
    }


    // build the Huffman trie given frequencies
    private static Node buildTrie(int[] freq) {

        // initialize priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char c = 0; c < R; c++) {
            if (freq[c] > 0)
                pq.insert(new Node(c, freq[c], null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();   // return the final root node.
    }


    // write bitstring-encoded trie to standard output
    // pre-order traversal.
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {               // if visits a leaf, it writes a 1 bit, followed by the 8-bit ASCII code
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch);
            return;
        }
        BinaryStdOut.write(false);   // if visits a internal node, it writes a single 0 bit
        writeTrie(x.left);             // visit left child node
        writeTrie(x.right);            // visit right child node
    }


    // pre-order traversal, read trie.
    // reconstructs the trie from the bitstring : it reads a single bit to learn which type of node comes
    // next: if a leaf(the bit is 1) it reads the next character and creates a leaf; if an internal node
    // (the bit is 0) it creates an internal node and then (recursively) builds its left and right subtrees.
    private static Node readTrie() {
        if (BinaryStdIn.readBoolean()) {   // if is leaf node
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        }
        else {
            Node x = readTrie();
            Node y = readTrie();
            return new Node('\0', -1, x, y);
        }
    }


    /**
     * Sample client that call {@code compress()} if the command-line argument is "-",
     * and {@code expand()} if it is "+".
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
