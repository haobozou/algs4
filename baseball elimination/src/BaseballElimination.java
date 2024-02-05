import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private final int numberOfTeams;
    private final int numberOfGameVertices;
    private final int source;
    private final int target;
    private final List<String> teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;
    private final boolean[] isEliminated;
    private final Map<Integer, Set<String>> certificateOfElimination;

    public BaseballElimination(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        In in = new In(filename);
        this.numberOfTeams = in.readInt();
        this.numberOfGameVertices = this.numberOfTeams * (this.numberOfTeams - 1) / 2;
        this.source = 0;
        this.target = numberOfGameVertices + this.numberOfTeams + 1;
        this.teams = new ArrayList<>();
        this.wins = new int[this.numberOfTeams];
        this.losses = new int[this.numberOfTeams];
        this.remaining = new int[this.numberOfTeams];
        this.against = new int[this.numberOfTeams][this.numberOfTeams];
        this.isEliminated = new boolean[this.numberOfTeams];
        this.certificateOfElimination = new HashMap<>();
        for (int i = 0; i < this.numberOfTeams; i++) {
            String team = in.readString();
            this.teams.add(team);
            this.wins[i] = in.readInt();
            this.losses[i] = in.readInt();
            this.remaining[i] = in.readInt();
            for (int j = 0; j < this.numberOfTeams; j++) {
                this.against[i][j] = in.readInt();
            }
        }
        String provisionalWinner = null;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < this.numberOfTeams; i++) {
            if (max < this.wins[i]) {
                provisionalWinner = this.teams.get(i);
                max = this.wins[i];
            }
        }
        for (int i = 0; i < this.numberOfTeams; i++) {
            if (this.wins[i] + this.remaining[i] < max) {
                this.isEliminated[i] = true;
                this.certificateOfElimination.put(i, Set.of(provisionalWinner));
            } else {
                maxFlow(buildFlowNetwork(i), i);
            }
        }
    }

    public int numberOfTeams() {
        return this.numberOfTeams;
    }

    public Iterable<String> teams() {
        return this.teams;
    }

    public int wins(String team) {
        validateTeam(team);
        return this.wins[this.teams.indexOf(team)];
    }

    public int losses(String team) {
        validateTeam(team);
        return this.losses[this.teams.indexOf(team)];
    }

    public int remaining(String team) {
        validateTeam(team);
        return this.remaining[this.teams.indexOf(team)];
    }

    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return this.against[this.teams.indexOf(team1)][this.teams.indexOf(team2)];
    }

    public boolean isEliminated(String team) {
        validateTeam(team);
        return this.isEliminated[this.teams.indexOf(team)];
    }

    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        return this.certificateOfElimination.get(this.teams.indexOf(team));
    }

    private FlowNetwork buildFlowNetwork(int index) {
        FlowNetwork flowNetwork = new FlowNetwork(this.target + 1);
        int game = 0;
        for (int i = 0; i < this.numberOfTeams; i++) {
            if (i == index) {
                game += this.numberOfTeams - i - 1;
                continue;
            }
            int capacity = this.wins[index] + this.remaining[index] - this.wins[i];
            flowNetwork.addEdge(new FlowEdge(this.numberOfGameVertices + i + 1, this.target, capacity));
            for (int j = i + 1; j < this.numberOfTeams; j++) {
                game++;
                if (j == index) {
                    continue;
                }
                flowNetwork.addEdge(new FlowEdge(this.source, game, this.against[i][j]));
                flowNetwork.addEdge(new FlowEdge(game, this.numberOfGameVertices + i + 1, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(game, this.numberOfGameVertices + j + 1, Double.POSITIVE_INFINITY));
            }
        }
        return flowNetwork;
    }

    private void maxFlow(FlowNetwork flowNetwork, int index) {
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, this.source, this.target);
        for (int i = 1; i < this.numberOfGameVertices + 1; i++) {
            if (fordFulkerson.inCut(i)) {
                this.isEliminated[index] = true;
                for (FlowEdge flowEdge : flowNetwork.adj(i)) {
                    if (i == flowEdge.to()) {
                        continue;
                    }
                    String eliminatingTeam = this.teams.get(flowEdge.to() - this.numberOfGameVertices - 1);
                    this.certificateOfElimination.putIfAbsent(index, new HashSet<>());
                    this.certificateOfElimination.get(index).add(eliminatingTeam);
                }
            }
        }
    }

    private void validateTeam(String team) {
        if (!this.teams.contains(team)) {
            throw new IllegalArgumentException();
        }
    }
}