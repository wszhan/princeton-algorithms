import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

    private static final String CSV_DELIMITER = ",";
    private static final String SYNSET_DELIMITER = " ";

    private final LinearProbingHashST<String, Bag<Integer>> synsetsTable;
    private final LinearProbingHashST<Integer, String> synsetsTableReversed;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        synsetsTable = new LinearProbingHashST<String, Bag<Integer>>();
        synsetsTableReversed = new LinearProbingHashST<Integer, String>();

        int numberOfVertices = 0;

        // 1. use synsets file to build symbol table string/word -> integer/index
        In synsetsIn = new In(synsets);
        while (synsetsIn.hasNextLine()) {
            String[] synsetLine = synsetsIn.readLine().split(CSV_DELIMITER);

            int id = Integer.parseInt(synsetLine[0]);
            synsetsTableReversed.put(id, synsetLine[1]);
            String[] synonyms = synsetLine[1].split(SYNSET_DELIMITER);
            for (String s : synonyms) {
                Bag<Integer> stringIds = synsetsTable.get(s);
                if (stringIds != null) {
                    stringIds.add(id);
                } else {
                    Bag<Integer> vals = new Bag<Integer>();
                    vals.add(id);
                    synsetsTable.put(s, vals);
                }
            }
            numberOfVertices++;
        }

        // 2. use hypernym file to build digraph
        Digraph graph = new Digraph(numberOfVertices);
        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String[] vertices = hypernymsIn.readLine().split(CSV_DELIMITER);
            int v = Integer.parseInt(vertices[0]);
            for (int i = 1; i < vertices.length; i++) {
                int w = Integer.parseInt(vertices[i]);
                graph.addEdge(v, w);
            }
        }

        // input graph must be rooted DAG
        // DirectedCycle checkCycle = new DirectedCycle(graph);
        // if (checkCycle.hasCycle()) throw new IllegalStateException();
        // NOTE that the root has no outgoing degree, the edge points from
        // synsets to hypernyms
        int rootIndex = -1;
        Topological checkRootedDAG = new Topological(graph);
        Iterable<Integer> orderedElements = checkRootedDAG.order();
        if (orderedElements == null) throw new IllegalStateException("not DAG");
        for (int i : orderedElements) {
            if (graph.outdegree(i) == 0) {
                if (rootIndex >= 0) {
                    throw new IllegalArgumentException("not rooted");
                } else {
                    rootIndex = i;
                }
            }
        }

        this.sap = new SAP(graph);
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsTable.keys();
    }


    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetsTable.contains(word);
    }


    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        Bag<Integer> v, w;
        v = synsetsTable.get(nounA);
        w = synsetsTable.get(nounB);

        int dist = sap.length(v, w);
        return dist;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        Bag<Integer> v, w;
        v = synsetsTable.get(nounA);
        w = synsetsTable.get(nounB);

        int ancestor = sap.ancestor(v, w);
        return synsetsTableReversed.get(ancestor);
    }

    public static void main(String[] args) {
        // System.out.printf("args length: %d\n", args.length);
        
        if (args != null && args.length == 2) {
            String synsetsFileName = args[0];
            String hypernymsFileName = args[1];

            WordNet wn = new WordNet(synsetsFileName, hypernymsFileName);
            // Digraph graph = wn.getGraph();
            assert wn.isNoun("aldsjf") == false;
            assert wn.isNoun("Aberdeen") == true;

            int dist;
            String wordA, wordB;
            wordA = "miracle";
            wordB = "event";
            dist = wn.distance(wordA, wordB);
            assert dist == 1;

            wordA = "event";
            wordB = "happening";
            dist = wn.distance(wordA, wordB);
            assert dist == 1;

            wordA = "miracle";
            wordB = "happening";
            dist = wn.distance(wordA, wordB);
            assert dist == 1;
        } else {
            String synsetsFileName = "synsets6.txt";
            String hypernymsFileName = "hypernyms6InvalidCycle.txt";

            WordNet wn = new WordNet(synsetsFileName, hypernymsFileName);
        }
    }
}
