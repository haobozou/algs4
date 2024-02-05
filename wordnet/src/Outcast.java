public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        String outcast = null;
        int max = Integer.MIN_VALUE;
        for (String nounA : nouns) {
            int sum = 0;
            for (String nounB : nouns) {
                sum += this.wordnet.distance(nounA, nounB);
            }
            if (sum > max) {
                outcast = nounA;
                max = sum;
            }
        }
        return outcast;
    }
}