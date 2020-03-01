
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

import java.util.ArrayList;
import java.util.HashMap;

public final class WordNet {
    private HashMap<Integer, String> map = new HashMap<>();   // HashMap between index and synsets
    private RedBlackBST<String, ArrayList<Integer>> nounsTree = new RedBlackBST<>(); // Key=noun, value = indices.
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("WordNet Constructor arguments are null");

        // Construct HashMap map.
        In inSynsets = new In(synsets);
        int numOfVertices = 0;
        while (!inSynsets.isEmpty()) {
            String line = inSynsets.readLine();
            // split by "," separate index and synset
            String[] fields = line.split(",");
            int i = Integer.parseInt(fields[0]);   // index
            map.put(i, fields[1]);                 // put key=index, value=synset into HashMap
            numOfVertices++;

            // separate  synset by space into multiple nouns.
            // key = noun, value = the indices where the noun occur
            String[] n = fields[1].split("\\s+");
            for (String x : n) {
                if (!nounsTree.contains(x)) {
                    ArrayList<Integer> integers = new ArrayList<>();
                    integers.add(i);   // add index to arrayList.
                    nounsTree.put(x, integers);
                }
                else {
                    nounsTree.get(x).add(i);  // add index to arrayList.
                }
            }
        }

        // Construct Digraph G
        Digraph G = new Digraph(numOfVertices);
        In inHypernyms = new In(hypernyms);
        while (!inHypernyms.isEmpty()) {
            String[] stringsFields = inHypernyms.readLine().split(",");
            int[] intFields = new int[stringsFields.length];
            // change the string fields to integer
            for (int i = 0; i < stringsFields.length; i++) {
               intFields[i] = Integer.parseInt(stringsFields[i]);
            }
            // add integer pairs into digraph
            for (int j = 1; j < intFields.length; j++) {
                G.addEdge(intFields[0], intFields[j]);
            }
        }

        // Construct SAP sap.
        /*
        !!! throw an Illegal Argument Exception when G is not a rooted DAG

         */
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsTree.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("isNoun argument is null");
        return nounsTree.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("distance argument is null");
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("nounA or nounB is not a WordNet noun");
        return sap.length(nounsTree.get(nounA), nounsTree.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("distance argument is null");
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("nounA or nounB is not a WordNet noun");
        int ancestor = sap.ancestor(nounsTree.get(nounA), nounsTree.get(nounB));
        return map.get(ancestor);
    }
}


