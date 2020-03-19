public class DicTries {
    private static final int R = 26;   // letters form A to Z
    private Node root;                 // root of trie
    private int n;                     // number of keys in trie.
//    private Node curr;                 // current Node.

    // 26-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    /**
     * Adds the key to the set if it is not already present
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} if {@code null}
     */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) n++;
            x.isString = true;
        }
        else {
            char c = key.charAt(d);
            x.next[c-'A'] = add(x.next[c-'A'], key, d+1);
        }
        return x;
    }

    public int size() { return n; }

    /**
     * Does the set contains keys with string prefix?
     * @param prefix the prefix
     * @return {@code true} if it contains keys with prefix, return {@code false} otherwise.
     */
    public boolean keysWithPrefix(StringBuilder prefix) {
        if (prefix == null) throw new IllegalArgumentException("argument ot keyWithPrefix() is null");
        Node x = get(root, prefix, 0);
        return (x != null);
    }

    public boolean contains(StringBuilder key) {
        if (key == null) throw new IllegalArgumentException("argument to contains is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, StringBuilder key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c-'A'], key ,d+1);
    }
}
