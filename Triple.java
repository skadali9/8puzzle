//Triple class holds the current matrix state, it's heuristic and depth
public class Triple {
    public int[][] matrix; // the state matrix
    public int heuristic; // the heuristic value
    public int depth; // the depth of the state in the search tree

    public Triple(int[][] matrix, int heuristic, int depth) {
        this.matrix = matrix;
        this.heuristic = heuristic;
        this.depth = depth;
    }
}
