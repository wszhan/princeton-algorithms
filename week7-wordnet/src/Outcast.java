import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException();
        
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int numberOfNouns = nouns.length;
        String outcastNoun = null;
        int outcastDistance = -1;

        for (int i = 0; i < numberOfNouns; i++) {
            String curr = nouns[i];
            int distance = 0;

            // compute distances against all others including this noun itself
            for (int j = 0; j < numberOfNouns; j++) {
                distance += wordnet.distance(curr, nouns[j]);
            }

            if (distance > outcastDistance) {
                outcastDistance = distance;
                outcastNoun = curr;
            }
        }

        return outcastNoun;
    }

    public static void main(String[] args) {
        if (args != null && args.length > 2) {
            WordNet wordnet = new WordNet(args[0], args[1]);
            Outcast outcast = new Outcast(wordnet);
            for (int t = 2; t < args.length; t++) {
                In in = new In(args[t]);
                String[] nouns = in.readAllStrings();
                StdOut.println(args[t] + ": " + outcast.outcast(nouns));
            }
        }
    }
}