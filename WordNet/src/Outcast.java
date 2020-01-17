import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;
    public Outcast (WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException("Outcast constructor argument is null");
        this.wordnet = wordnet;
    }

    public String outcast(String nouns[]) {
        if (nouns == null) throw new IllegalArgumentException("outcast argument is null");
        int[] sumOfDistance = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            sumOfDistance[i] = 0;
            for (int j = 0; j < nouns.length; j++) {
                sumOfDistance[i] += wordnet.distance(nouns[i], nouns[j]);
            }
        }
        int maxDistance = 0, maxIndex = 0;
        for (int k = 0; k < nouns.length; k++) {
            if (sumOfDistance[k] > maxDistance) {
                maxDistance = sumOfDistance[k];
                maxIndex = k;
            }
        }
        return nouns[maxIndex];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
