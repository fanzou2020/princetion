import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private DicTries trie;

    /**
     * Initializes the data structure using the given array of strings as the dictionary.
     * (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
     * @param dictionary string array of dictionary
     */
    public BoggleSolver(String[] dictionary) {
        trie = new DicTries();
        for (String s : dictionary)
            trie.add(s);
//        StdOut.println(trie.size());
    }

    /**
     * Return the set off valid words in the given Boggle board, as an Iterable
     * @param board given Boggle board
     * @return the set off valid words in the given Boggle board, as an Iterable
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int rows = board.rows();
        int cols = board.cols();
        SET<String> allValidWords = new SET<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean[][] onStack = new boolean[rows][cols];
                StringBuilder stringBuilder = new StringBuilder();
                dfs(board, i, j, onStack, stringBuilder, allValidWords);
            }
        }
        return allValidWords;
    }


    // Starting at character at board[i][j], find the strings that can are reachable from board[i][j] and
    // are in the dictionary, store them in the allValidWords SET.
    private void dfs(BoggleBoard board, int i, int j, boolean[][] onStack, StringBuilder s, SET<String> allValidWords) {
        onStack[i][j] = true;
        char letter = board.getLetter(i, j);
        if (letter == 'Q') {
            s.append("QU");
        } else {
            s.append(letter);
        }

        if (!trie.keysWithPrefix(s)) {
            if (letter == 'Q') s.deleteCharAt(s.length()-1);
            s.deleteCharAt(s.length()-1);
            onStack[i][j] = false;
            return;
        }

        if (trie.contains(s) && s.length() >= 3) allValidWords.add(s.toString());

        // all adjacent cubes
        int[][] adj = {
                {i - 1, j - 1}, {i - 1, j}, {i - 1, j + 1},
                {i    , j - 1},             {i    , j + 1},
                {i + 1, j - 1}, {i + 1, j}, {i + 1, j + 1}
        };


        int rows = board.rows(), cols = board.cols();
        for (int k = 0; k < 8; k++) {
            if (adj[k][0] < 0 || adj[k][0] >= rows) continue;
            if (adj[k][1] < 0 || adj[k][1] >= cols) continue;
            if (onStack[adj[k][0]][adj[k][1]]) continue;
            else {
                dfs(board, adj[k][0], adj[k][1], onStack, s, allValidWords);
            }
        }

        onStack[i][j] = false;
        if (letter == 'Q') s.deleteCharAt(s.length()-1);
        s.deleteCharAt(s.length()-1);
    }

    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise.
     * @param word the given word
     * @return the score of the given word
     */
    public int scoreOf(String word) {
        int len = word.length();
        if (len < 3) return 0;

        StringBuilder s = new StringBuilder(word);
        if (!trie.contains(s)) return 0;

        if (len <= 4) return 1;
        else if (len == 5) return 2;
        else if (len == 6) return 3;
        else if (len == 7) return 5;
        else return 11;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
