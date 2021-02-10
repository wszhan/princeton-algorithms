import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private Stack<Board> solutionTrace = null;
    private int solutionMoves = -1;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // validate input
        // validation should be at the VERY BEGINNING
        if (initial == null || initial.dimension() == 0) {
            throw new IllegalArgumentException("Solver - invalid input");
        }

        int initialBoardManhattan = initial.manhattan();

        // PQs are only used in the constructor
        // no need to create them as instance variables
        MinPQ<SearchNode> gameTreeMain = new MinPQ<>(initialBoardManhattan);
        MinPQ<SearchNode> gameTreeTwin = new MinPQ<>(initialBoardManhattan);

        // initialization of the game tree 
        SearchNode rootNode = new SearchNode(null, initial, 0);
        gameTreeMain.insert(rootNode);

        // twin game tree to find unsolvable board
        // no need to keep track
        Board twinBoard = initial.twin();
        SearchNode twinRootNode = new SearchNode(null, twinBoard, 0);
        gameTreeTwin.insert(twinRootNode);

        // repeat until a goal board is found
        // alternating between main game tree and twin game tree
        boolean searchMain = true;
        // if solution node is null after while loop,
        // that means solution is found for twin
        // and the initial board is unsolvable
        SearchNode solutionNode = null;

        while (true) {
            MinPQ<SearchNode> gameTree;

            if (searchMain) { 
                gameTree = gameTreeMain;
            } else {
                gameTree = gameTreeTwin;
            }

            SearchNode dequeuedNode = gameTree.delMin();
            Board currentBoard = dequeuedNode.currBoard;
            SearchNode prevNode = dequeuedNode.prevNode;

            // break if a goal board is found
            // if main tree is currently being search, assign the node to the solution
            // otherwise just breka and leave the solution null
            if (currentBoard.isGoal()) {
                if (searchMain) {
                    solutionNode = dequeuedNode;
                }
                break;
            }

            // if the board in the current search node isn't a goal board
            // put all its neighbors() in the MinPQ
            // after construct each of them into a search node
            // but don't do so if the neighbor baord is identical to that board in 
            // the previous node 
            for (Board neighbor : currentBoard.neighbors()) {
                if (prevNode == null || !neighbor.equals(prevNode.currBoard)) {
                    SearchNode node = new SearchNode(dequeuedNode, neighbor, dequeuedNode.moves+1);
                    gameTree.insert(node);
                }
            }

            // alternating
            searchMain = !searchMain;
        }

        // build trace reversely with stack
        if (solutionNode != null) {
            solutionTrace = new Stack<>();
            this.solutionMoves = solutionNode.moves;
            SearchNode curr = solutionNode;

            while (curr != null) {
                solutionTrace.push(curr.currBoard);
                curr = curr.prevNode;
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        SearchNode prevNode;
        Board currBoard;

        // instance variables on priorities
        int moves;
        int hamming;
        int manhattan;

        private SearchNode(SearchNode prevNode, Board currBoard, int movesAlreadyMade) {
            // prevNode is allowed to be null in the case of root search node
            if (currBoard == null || movesAlreadyMade < 0) {
                throw new IllegalArgumentException("SearchNode - Illegal arguments");
            }

            this.prevNode = prevNode;
            this.currBoard = currBoard;
            this.moves = movesAlreadyMade;
            this.hamming = this.currBoard.hamming();
            this.manhattan = this.currBoard.manhattan();
        }

        /**
         * Manhattan priority.
         */
        private int manhattanPriority() {
            return this.moves + this.manhattan;
        }

        /**
         * Prioritize moves while comparing search nodes.
         */
        public int compareTo(SearchNode that) {
            int thisMP = this.manhattanPriority();
            int thatMP = that.manhattanPriority();

            if (thisMP > thatMP) { 
                return 1;
            } else if (thisMP < thatMP) {
                return -1;
            } else {
                if (this.manhattan > that.manhattan) return 1;
                else if (this.manhattan < that.manhattan) return -1;
                else {
                    if (this.hamming > that.hamming) return 1;
                    else if (this.hamming < that.hamming) return -1;
                }
            }

            return 0;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        boolean goalBoardFound = solutionTrace != null;
        // boolean goalBoardFound = !solutionTrace.isEmpty();
        return goalBoardFound;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        int movesToGoal = this.solutionMoves;
        return movesToGoal;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutionTrace;
    }

    // test client (see below)
    public static void main(String[] args) {
        boolean specificTest = false;

        In in; 
        if (specificTest) {
            // in = new In("puzzle05.txt");
            // in = new In("puzzle2x2-unsolvable1.txt");
            in = new In("puzzle01.txt");
        } else {
            in = new In(args[0]);
        }

        // create initial board from file
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
    }
}
