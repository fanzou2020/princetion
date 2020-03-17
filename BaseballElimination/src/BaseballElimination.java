import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

/*
按照要求建立好flow network, 运行Fordfolkson算法即可，注意点在于game vertice 和 tema vertices 对应的整数序号 v.
 */
public class BaseballElimination {
    private final int n;
    private final String[] teams;
    private final int[] w;                    // w[i] is the wins of team i
    private final int[] l;                    // l[i] is the losses of team i
    private final int[] r;                    // r[i] is the remaining games of team i
    private final int[][] g;                  // g[i][j] games left to play against between team i and team j
    private boolean[] isEliminated;     // isEliminated[i] indicates team[i] is eliminated or not
    private final ArrayList<String>[] certificateOfElimination;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        teams = new String[n];
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        isEliminated = new boolean[n];
        certificateOfElimination = (ArrayList<String>[]) new ArrayList[n];

        for (int i = 0; i < n; i++) {
            certificateOfElimination[i] = new ArrayList<>();
            teams[i] = in.readString();
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++)
                g[i][j] = in.readInt();
        }

        // process max flow network
        for (int i = 0; i < n; i++) {
            process(teams[i]);
        }
    }

    private void process(String name) {
        int x = findTeam(name);     // the index of this team

        // remove team x in teams[] and g[][] and w[]
        String[] teamNew = new String[n-1];
        int[][] gNew = new int[n-1][n-1];
        int[] wNew = new int[n-1];
        for (int i = 0; i < n-1; i++) {
            if (i < x) {
                teamNew[i] = teams[i];
                wNew[i] = w[i];
            }
            else {
                teamNew[i] = teams[i+1];
                wNew[i] = w[i+1];
            }
        }
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - 1; j++) {
                if      (i < x && j < x)    gNew[i][j] = g[i][j];
                else if (i < x && j >= x)   gNew[i][j] = g[i][j + 1];
                else if (i >= x && j < x)   gNew[i][j] = g[i + 1][j];
                else if (i >= x && j >= x)  gNew[i][j] = g[i + 1][j + 1];
            }
        }


        // First for trivial elimination
        for (int i = 0; i < n-1; i++) {
            if (w[x] + r[x] < w[i]) {
                isEliminated[x] = true;
                certificateOfElimination[x].add(teams[i]);
            }
        }
        if (isEliminated[x]) return;


        // Non-trivial elimination
        // total number of vertices in network = source + game vertices + team vertices + sink
        int V = 1 + (n-1)*(n-2)/2 + (n-1) + 1;
        FlowNetwork flowNetwork = new FlowNetwork(V);

        // add edges to flow network
        // sourceVertex = 0, gameVertex = 1 is the first game vertex, teamVertex is the first team vertex
        // sinkVertex = V - 1;
        int gameVertex = 1;
        int teamVertex = 1 + (n-1)*(n-2)/2;
        int sinkVertex = V - 1;
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n-1; j++) {
                flowNetwork.addEdge(new FlowEdge(0, gameVertex, gNew[i][j]));
                flowNetwork.addEdge(new FlowEdge(gameVertex, teamVertex+i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(gameVertex, teamVertex+j, Double.POSITIVE_INFINITY));
                gameVertex++;
            }
        }

        for (int i = 0; i < n-1; i++) {
            int c = w[x] + r[x] - wNew[i];
            int capacity = Math.max(c, 0);
            flowNetwork.addEdge(new FlowEdge(teamVertex + i, sinkVertex, capacity));
        }

        //**************** end of construct network flow *************************//

        // perform max flow algorithm to find elimination
        FordFulkerson ff = new FordFulkerson(flowNetwork, 0, V-1);

        for (int i = 0; i < n-1; i++) {
            if (ff.inCut(teamVertex+i)) {
                isEliminated[x] = true;
                certificateOfElimination[x].add(teamNew[i]);
            }
        }
    }

    // number of teams
    public int numberOfTeams() { return n; }

    // return all the teams
    public Iterable<String> teams() {
        ArrayList<String> returnTeams = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            returnTeams.add(teams[i]);
        }
        return returnTeams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null) throw new IllegalArgumentException("team name is null");
        return w[findTeam(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null) throw new IllegalArgumentException("team name is null");
        return l[findTeam(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null) throw new IllegalArgumentException("team name is null");
        return r[findTeam(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null) throw new IllegalArgumentException("team name is null");
        if (team2 == null) throw new IllegalArgumentException("team name is null");
        return g[findTeam(team1)][findTeam(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null) throw new IllegalArgumentException("team name is null");
        return isEliminated[findTeam(team)];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) throw new IllegalArgumentException("team name is null");
        int index = findTeam(team);
        return isEliminated(team) ? certificateOfElimination[index] : null;
    }



    // find and validate team name
    private int findTeam (String teamName) {
        for (int i = 0; i < n; i++) {
            if (teamName.equals(teams[i])) return i;
        }
        throw new IllegalArgumentException("invalid team name");
    }


    // Unit test
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
