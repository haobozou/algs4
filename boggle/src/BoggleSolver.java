import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoggleSolver {
    private static final int R = 26;
    private final Trie trie;
    private Die[][] dice;

    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException();
        }
        this.trie = new Trie();
        for (String word : dictionary) {
            this.trie.put(word, lengthToScore(word.length()));
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        initDice(board);
        Set<String> words = new HashSet<>();
        for (int i = 0; i < this.dice.length; i++) {
            for (int j = 0; j < this.dice[0].length; j++) {
                char letter = this.dice[i][j].letter;
                collect(this.dice[i][j], this.trie.root.next[letter - 'A'], "" + letter, words);
            }
        }
        return words;
    }

    public int scoreOf(String word) {
        return this.trie.get(word);
    }

    private void initDice(BoggleBoard board) {
        int[][] offsets = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
        this.dice = new Die[board.rows()][board.cols()];
        for (int i = 0; i < this.dice.length; i++) {
            for (int j = 0; j < this.dice[0].length; j++) {
                this.dice[i][j] = new Die(board.getLetter(i, j));
            }
        }
        for (int i = 0; i < this.dice.length; i++) {
            for (int j = 0; j < this.dice[0].length; j++) {
                for (int[] offset : offsets) {
                    if (isIndicesValid(i + offset[0], j + offset[1])) {
                        this.dice[i][j].neighbors.add(this.dice[i + offset[0]][j + offset[1]]);
                    }
                }
            }
        }
    }

    private void collect(Die die, Node node, String prefix, Set<String> words) {
        if (node == null) {
            return;
        }
        if (die.letter == 'Q') {
            if (node.next['U' - 'A'] == null) {
                return;
            }
            node = node.next['U' - 'A'];
            prefix += 'U';
        }
        if (node.score != 0) {
            words.add(prefix);
        }
        die.marked = true;
        for (Die neighbor : die.neighbors) {
            if (neighbor.marked) {
                continue;
            }
            collect(neighbor, node.next[neighbor.letter - 'A'], prefix + neighbor.letter, words);
        }
        die.marked = false;
    }

    private boolean isIndicesValid(int row, int col) {
        return row >= 0 && row <= this.dice.length - 1 && col >= 0 && col <= this.dice[0].length - 1;
    }

    private int lengthToScore(int length) {
        switch (length) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    private static class Trie {
        private Node root;

        public Trie() {
            this.root = new Node();
        }

        public void put(String word, int score) {
            this.root = put(this.root, word, score, 0);
        }

        private Node put(Node node, String word, int score, int d) {
            if (node == null) {
                node = new Node();
            }
            if (d == word.length()) {
                node.score = score;
                return node;
            }
            char c = word.charAt(d);
            node.next[c - 'A'] = put(node.next[c - 'A'], word, score, d + 1);
            return node;
        }

        public int get(String word) {
            Node node = get(this.root, word, 0);
            if (node == null) {
                return 0;
            }
            return node.score;
        }

        private Node get(Node node, String word, int d) {
            if (node == null) {
                return null;
            }
            if (d == word.length()) {
                return node;
            }
            char c = word.charAt(d);
            return get(node.next[c - 'A'], word, d + 1);
        }
    }

    private static class Node {
        private int score;
        private final Node[] next;

        public Node() {
            this.score = 0;
            this.next = new Node[R];
        }
    }

    private static class Die {
        private final char letter;
        private final List<Die> neighbors;
        private boolean marked;

        public Die(char letter) {
            this.letter = letter;
            this.neighbors = new ArrayList<>();
            this.marked = false;
        }
    }
}