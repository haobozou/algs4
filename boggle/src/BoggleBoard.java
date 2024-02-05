import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class BoggleBoard {
    private static final String[] BOGGLE_1992 = {
            "LRYTTE", "VTHRWE", "EGHWNE", "SEOTIS",
            "ANAEEG", "IDSYTT", "OATTOW", "MTOICU",
            "AFPKFS", "XLDERI", "HCPOAS", "ENSIEU",
            "YLDEVR", "ZNRNHL", "NMIQHU", "OBBAOJ"
    };
    private static final String[] BOGGLE_1983 = {
            "AACIOT", "ABILTY", "ABJMOQ", "ACDEMP",
            "ACELRS", "ADENVZ", "AHMORS", "BIFORX",
            "DENOSW", "DKNOTU", "EEFHIY", "EGINTV",
            "EGKLUY", "EHINPS", "ELPSTU", "GILRUW",
    };
    private static final String[] BOGGLE_MASTER = {
            "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
            "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
            "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR",
            "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
            "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
    };
    private static final String[] BOGGLE_BIG = {
            "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
            "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCENST",
            "CEIILT", "CEILPT", "CEIPST", "DDHNOT", "DHHLOR",
            "DHLNOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
            "FIPRSY", "GORRVW", "IPRRRY", "NOOTUW", "OOOTTU"
    };
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final double[] FREQUENCIES = {
            0.08167, 0.01492, 0.02782, 0.04253, 0.12703, 0.02228,
            0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025,
            0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987,
            0.06327, 0.09056, 0.02758, 0.00978, 0.02360, 0.00150,
            0.01974, 0.00074
    };
    private final int m;
    private final int n;
    private final char[][] board;

    public BoggleBoard() {
        this.m = 4;
        this.n = 4;
        StdRandom.shuffle(BOGGLE_1992);
        this.board = new char[this.m][this.n];
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                String letters = BOGGLE_1992[this.n * i + j];
                int r = StdRandom.uniformInt(letters.length());
                this.board[i][j] = letters.charAt(r);
            }
        }
    }

    public BoggleBoard(String filename) {
        In in = new In(filename);
        this.m = in.readInt();
        this.n = in.readInt();
        if (this.m <= 0 || this.n <= 0) {
            throw new IllegalArgumentException();
        }
        this.board = new char[this.m][this.n];
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                String letter = in.readString().toUpperCase();
                if (letter.equals("QU")) {
                    this.board[i][j] = 'Q';
                } else if (letter.length() != 1 || !ALPHABET.contains(letter)) {
                    throw new IllegalArgumentException();
                } else {
                    this.board[i][j] = letter.charAt(0);
                }
            }
        }
    }

    public BoggleBoard(int m, int n) {
        this.m = m;
        this.n = n;
        if (m <= 0 || n <= 0) {
            throw new IllegalArgumentException();
        }
        this.board = new char[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int r = StdRandom.discrete(FREQUENCIES);
                this.board[i][j] = ALPHABET.charAt(r);
            }
        }
    }

    public BoggleBoard(char[][] a) {
        this.m = a.length;
        if (this.m == 0) {
            throw new IllegalArgumentException();
        }
        this.n = a[0].length;
        if (this.n == 0) {
            throw new IllegalArgumentException();
        }
        this.board = new char[this.m][this.n];
        for (int i = 0; i < this.m; i++) {
            if (a[i].length != this.n) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < this.n; j++) {
                if (ALPHABET.indexOf(a[i][j]) == -1) {
                    throw new IllegalArgumentException();
                }
                this.board[i][j] = a[i][j];
            }
        }
    }

    public int rows() {
        return this.m;
    }

    public int cols() {
        return this.n;
    }

    public char getLetter(int i, int j) {
        return this.board[i][j];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.m + " " + this.n + "\n");
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                sb.append(this.board[i][j]);
                if (this.board[i][j] == 'Q') {
                    sb.append("u ");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }
}