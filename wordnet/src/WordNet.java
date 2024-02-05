import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final List<String[]> synsetsList;
    private final Map<String, Set<Integer>> synsetsMap;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        In in;
        this.synsetsList = new ArrayList<>();
        this.synsetsMap = new HashMap<>();
        in = new In(synsets);
        while (in.hasNextLine()) {
            String[] strings = in.readLine().split(",");
            String[] nouns = strings[1].split(" ");
            this.synsetsList.add(nouns);
            for (String noun : nouns) {
                this.synsetsMap.putIfAbsent(noun, new HashSet<>());
                this.synsetsMap.get(noun).add(Integer.parseInt(strings[0]));
            }
        }
        Digraph synsetsDigraph = new Digraph(this.synsetsList.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] strings = in.readLine().split(",");
            for (int i = 1; i < strings.length; i++) {
                synsetsDigraph.addEdge(Integer.parseInt(strings[0]), Integer.parseInt(strings[i]));
            }
        }

        DirectedCycle directedCycle = new DirectedCycle(synsetsDigraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException();
        }
        int numberOfRoots = 0;
        for (int i = 0; i < synsetsDigraph.V(); i++) {
            if (synsetsDigraph.outdegree(i) == 0) {
                numberOfRoots++;
            }
        }
        if (numberOfRoots != 1) {
            throw new IllegalArgumentException();
        }
        this.sap = new SAP(synsetsDigraph);
    }

    public Iterable<String> nouns() {
        return this.synsetsMap.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return this.synsetsMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Iterable<Integer> v = this.synsetsMap.get(nounA);
        Iterable<Integer> w = this.synsetsMap.get(nounB);
        return this.sap.length(v, w);
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Iterable<Integer> v = this.synsetsMap.get(nounA);
        Iterable<Integer> w = this.synsetsMap.get(nounB);
        int index = this.sap.ancestor(v, w);
        return String.join(" ", this.synsetsList.get(index));
    }
}