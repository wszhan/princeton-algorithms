import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    private ArrayList<Board> solutionTrace = null;
    private int solutionMoves = -1;

    // debugging
    private ArrayList<SearchNode> solutionNodes = new ArrayList<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        // PQs are only used in the constructor
        // no need to create them as instance variables
        MinPQ<SearchNode> gameTreeMain = new MinPQ<>();
        MinPQ<SearchNode> gameTreeTwin = new MinPQ<>();

        if (initial == null || initial.dimension() == 0) {
            throw new IllegalArgumentException("Solver - invalid input");
        }


        // initialization of the game tree 
        SearchNode rootNode = new SearchNode(null, initial, 0);
        gameTreeMain.insert(rootNode);
        // SearchNode dequedNodeMain = gameTreeMain.delMin();

        // twin game tree to find unsolvable board
        // no need to keep track
        Board twinBoard = initial.twin();
        SearchNode twinRootNode = new SearchNode(null, twinBoard, 0);
        gameTreeTwin.insert(twinRootNode);
        // SearchNode dequedNodeTwin = gameTreeTwin.delMin();

        // repeat until a goal board is found
        // alternating between main game tree and twin game tree
        boolean searchMain = true;
        // boolean twinGoalFound = false;
        SearchNode solutionNode = null;

        while (true) {
            MinPQ<SearchNode> gameTree;

            if (searchMain) { 
                // currentBoard = dequedNodeMain.currBoard;
                // dequedNode = dequedNodeMain;
                gameTree = gameTreeMain;
            } else {
                // currentBoard = dequedNodeTwin.currBoard;
                // dequedNode = dequedNodeTwin;
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

        if (solutionNode != null) {
            solutionTrace = new ArrayList<>();
            this.solutionMoves = solutionNode.moves;
            SearchNode curr = solutionNode;

            while (curr != null) {
                solutionTrace.add(curr.currBoard);
                curr = curr.prevNode;
            }

            Collections.reverse(solutionTrace);
        }

        // Feb 10, 2021
        // only one solution is needed. solution() returns traces.
        // Feb 09, 2021
        // dequeue nodes until it is not a goal board
        // to make sure all goal boards are found
        // Could be redundant?
        // while (solutionNode != null 
                // && solutionNode.currBoard.isGoal() 
                // && solutionNode.moves == this.solutionMoves
                // && !gameTreeMain.isEmpty()) {
            // solutionNodes.add(solutionNode);
            // goalBoards.add(solutionNode.currBoard);
            // solutionNode = gameTreeMain.delMin();
        // }
    }

    // public void printTrace() {
        // int count = 0;
        // for (SearchNode node : solutionNodes) {
            // count++;
            // SearchNode currNode = node;
            // Board currentBoard = currNode.currBoard;
            // System.out.printf("Trace of Solution #%d:\n", count);
            // do {
                // System.out.printf("Moves already made: %d\nManhattan: %d\nHamming: %d\n",
                    // currNode.moves,
                    // currNode.manhattan,
                    // currNode.hamming
                    // );
                // System.out.println(currNode.currBoard);
                // currNode = currNode.prevNode;
            // } while (currNode != null);
        // }
    // }

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
         * Prioritize moves while comparing search nodes.
         */
        public int compareTo(SearchNode that) {
            if (this.moves > that.moves) { 
                return 1;
            } else if (this.moves < that.moves) {
                return -1;
            } else if (this.hamming == that.hamming) {
                // break ties
                if (this.manhattan > that.manhattan) return 1;
                else if (this.manhattan < that.manhattan) return -1;
            } else if (this.manhattan == that.manhattan) {
                // break ties
                if (this.hamming > that.hamming) return 1;
                else if (this.hamming < that.hamming) return -1;
            } else {
                if (this.manhattan > that.manhattan) return 1;
                else if (this.manhattan < that.manhattan) return -1;
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
        int shortestPathMoves = this.solutionMoves;
        return shortestPathMoves;
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

            // test trace
            int count = 0;
            for (Board b : solver.solution()) {
                System.out.printf("move #%d:\n%s", count, b);
                count++;
            }

            // for (Board board : solver.solution())
                // StdOut.println(board);
        }
    }
}
