import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class TrieSET {
    private static final int R = 256;   // extended ASCII

    private Node root;  // root of trie
    private int n;      // number of keys in trie

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    /**
     * Initializes an empty set of strings
     */
    public TrieSET() {
    }

    /**
     * Does the set contain the given key?
     * @param key the key
     * @return {@code true} if the set contains {@code key} and {@code false} otherwise.
     * @throws IllegalArgumentException if {@code key} is null
     */
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

    /**
     * Adds the key to the set if it is not already present
     * @param key the key to add.
     * @throws IllegalArgumentException if {@code key} is {@code null}.
     */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) n++;   // if not already present, add size by 1.
            x.isString = true;      // set isString to true
        }
        else {
            char c = key.charAt(d);
            x.next[c] = add(x.next[c], key, d+1);
        }
        return x;
    }


    /**
     * Returns the number of strings in the set.
     * @return the number of strings in the set.
     */
    public int size() {
        return n;
    }

    /**
     * Is the set empty?
     * @return {@code true} if the set is empty, and {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size() == 0;
    }


//    public Iterator<String> iterator() {
//
//
//    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix}.
     */
    public Iterable<String> keyWithPrefix(String prefix) {
        Queue<String> results = new Queue<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.isString) results.enqueue(prefix.toString());    // if is key, add to queue
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }


    /**
     * Returns all of the keys in the set that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the set that match {@code pattern},
     *  as an iterable, where . symbol is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<>();
        StringBuilder prefix = new StringBuilder();
        collect(root, prefix, pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.isString)  // find the match
            results.enqueue(prefix.toString());
        if (d == pattern.length())                // cannot find the match
            return;
        char c = pattern.charAt(d);               // go to the next character in pattern.
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }


    /**
     * Returns the string in the set that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * E.g. {she, shells} are in the set
     * longestPrefixOf("she") = "she"
     * longestPrefixOf("shell") = "she"
     * longestPrefixOf("shellsort") = "shells".
     * @param query
     * @return
     */
    public String longestPrefixOf(String query) {
        if (query == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        return query.substring(0, length);
    }

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of length length
    private int longestPrefixOf(Node x, String query, int d,int length) {
        if (x == null) return length;
        if (x.isString) length = d;         // find a string, update length.
        if (d == query.length()) return length; // end of query string, return the length
        char c = query.charAt(d);           // go to the next char in query.
        return longestPrefixOf(x.next[c], query, d+1, length);
    }


    /**
     * Removes the key from the set if the key is present.
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     * }
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.isString) n--;  // if find, decrease size by 1.
            x.isString = false;
        }

        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.isString) return x;   // if x is string, return x

        for (int c = 0; c < R; c++) // if node x still have child nodes, keep these child nodes
            if (x.next[c] != null) return x;
        return null;    // if x has no child node, remove x by returning null.
    }







}
