import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;

public class Solver {

    private MinPQ<SearchNode> minPriorityQueue = new MinPQ<>();;
    private ArrayList<Board> goalBoards;
    private int solutionMoves = -1;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException("Solver - invalid input");
        }

        goalBoards = new ArrayList<>();

        // initialization of the priority queue
        SearchNode rootNode = new SearchNode(null, initial, 0);
        minPriorityQueue.insert(rootNode);
        SearchNode dequedNode = minPriorityQueue.delMin();

        // repeat until a goal board is found
        while (!dequedNode.currBoard.isGoal()) {
            Board currentBoard = dequedNode.currBoard;

            // if the board in the current search node isn't a goal board
            // put all its neighbors() in the MinPQ
            // after contruct each of them into a search node
            // but don't do so if the baord is identical to that board in 
            // the previous node 
            for (Board neighbor : currentBoard.neighbors()) {
                if (!neighbor.equals(currentBoard)) {
                    SearchNode node = new SearchNode(dequedNode, neighbor, dequedNode.moves);
                    minPriorityQueue.insert(node);
                }
            }

            dequedNode = minPriorityQueue.delMin();
        }


        solutionMoves = dequedNode.moves;

        // dequeue nodes until it is not a goal board
        // to make sure all goal boards are found
        while (dequedNode != null 
                && dequedNode.currBoard.isGoal() 
                && dequedNode.moves == this.solutionMoves) {
            goalBoards.add(dequedNode.currBoard);
            dequedNode = minPriorityQueue.delMin();
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
            this.currBoard= currBoard;
            this.moves = movesAlreadyMade + 1;
            this.hamming = this.currBoard.hamming();
            this.manhattan = this.currBoard.manhattan();
        }

        public int compareTo(SearchNode that) {
            if (this.moves > that.moves) return 1;
            else if (this.moves < that.moves) return -1;
            return 0;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        boolean goalBoardFound = !goalBoards.isEmpty();
        return goalBoardFound;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        int shortestPathMoves = this.solutionMoves;
        return shortestPathMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return goalBoards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
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
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
