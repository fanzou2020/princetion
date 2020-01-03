import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

public class Solver {
    private boolean solvable;
    private int numOfMove;
    private Stack<Board> solution = new Stack<>();


    private class SearchNode {
        Board curBoard;
        SearchNode predecessor;
        int move;
        int manhattan;

        public SearchNode(Board curBoard, SearchNode predecessor, int move, int manhattan) {
            this.curBoard = curBoard;
            this.predecessor = predecessor;
            this.move = move;
            this.manhattan = manhattan;
        }

    }

    // compares two search node by the manhattan value.
    private class NewComparator implements Comparator<SearchNode> {
        public int compare(SearchNode v, SearchNode w) {
            if (v.manhattan == w.manhattan) return 0;
            else if (v.manhattan < w.manhattan) return -1;
            else return +1;
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        // initial node & its twin node.
        SearchNode initialNode = new SearchNode(initial, null, 0, initial.manhattan());

        SearchNode initialTwinNode = new SearchNode(initial.twin(), null, 0, initial.manhattan());

        // Construct the priority queue using the comparator.
        // insert the initialNode
        NewComparator comparator = new NewComparator();
        MinPQ<SearchNode> pq = new MinPQ<>(comparator);
        pq.insert(initialNode);
        MinPQ<SearchNode> pqTwin = new MinPQ<>(comparator);
        pqTwin.insert(initialTwinNode);

        // The node enqueued from the priority queue.
        SearchNode min, minTwin;

        //
        do {
            min = pq.delMin();
            minTwin = pqTwin.delMin();

            // if min is goal, set solvable as true, then break;
            // if twin is solvable, means that initial is unsolvable.
            if (min.curBoard.isGoal()) {
                solvable = true;         // set solvable to true
                numOfMove = min.move;    // total number of moves.
                break;
            }
            else if (minTwin.curBoard.isGoal()) {
                solvable = false;
                numOfMove = -1;
                break;
            }

            // Insert the neighboring search node to the priority queue.
            Iterable<Board> neighbors = min.curBoard.neighbors();
            for (Board a : neighbors) {
                // if min = initialNode:
                if ((min == initialNode) && (min.predecessor == null)) {
                    SearchNode temp = new SearchNode(a, min, min.move+1, a.manhattan()+min.move+1);
                    pq.insert(temp);
                }

                // if neighbor equals to the predecessor, do not add it on pq.
                else if (!a.equals(min.predecessor.curBoard)) {
                    SearchNode temp = new SearchNode(a, min, min.move+1, a.manhattan()+min.move+1);
                    pq.insert(temp);
                }
            }

            Iterable<Board> neighborsTwin = minTwin.curBoard.neighbors();
            for (Board a : neighborsTwin) {
                // if minTwin == initialTwinNode:
                if ((minTwin == initialTwinNode) && (minTwin.predecessor == null)) {
                    SearchNode temp = new SearchNode(a, minTwin, minTwin.move+1,
                            a.manhattan()+minTwin.move+1);
                    pqTwin.insert(temp);
                }
                // if neighbor equals to the predecessor, do not add it on pq.
                else if (!a.equals(minTwin.predecessor.curBoard)) {
                    SearchNode temp = new SearchNode(a, minTwin, minTwin.move+1,
                            a.manhattan()+minTwin.move+1);
                    pqTwin.insert(temp);
                }
            }
        } while (true);

        // trace the solution
        if (solvable) {
            while (min != null) {
                solution.push(min.curBoard);
                min = min.predecessor;
            }
        }
        else {
            solution = null;
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return numOfMove;
    }

    public Iterable<Board> solution() {
        return solution;
    }
}
